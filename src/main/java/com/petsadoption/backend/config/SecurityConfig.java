package com.petsadoption.backend.config;

import com.petsadoption.backend.security.JwtAuthenticationEntryPoint;
import com.petsadoption.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad para la aplicación PETFRIENDLY.
 * Integra Spring Security con JWT tokens de Supabase Auth.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configuración principal de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF para API REST
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar manejo de excepciones de autenticación
            .exceptionHandling(exception -> 
                exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Configurar política de sesiones (stateless para JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Endpoints públicos de lectura
                .requestMatchers(HttpMethod.GET, "/api/v1/pets/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/foundations/**").permitAll()
                
                // Endpoints de contacto (público)
                .requestMatchers(HttpMethod.POST, "/api/v1/contact/**").permitAll()
                
                // Endpoints que requieren autenticación
                .requestMatchers("/api/v1/me/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/adoptions/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/adoptions/mine").authenticated()
                
                // Endpoints para administradores de fundación
                .requestMatchers(HttpMethod.POST, "/api/v1/pets/**").hasRole("FOUNDATION_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/pets/**").hasRole("FOUNDATION_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/pets/**").hasRole("FOUNDATION_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/foundations/**").hasRole("FOUNDATION_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/adoptions/by-foundation").hasRole("FOUNDATION_ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/adoptions/**").hasRole("FOUNDATION_ADMIN")
                
                // Endpoints para super admin
                .requestMatchers("/api/v1/admin/**").hasRole("SUPER_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/foundations").hasRole("SUPER_ADMIN")
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            );

        // Agregar filtro JWT antes del filtro de autenticación por username/password
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde el frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir orígenes específicos (ajustar según el entorno)
        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "https://*.vercel.app",
            "https://*.netlify.app",
            "https://petfriendly.com",
            "https://*.petfriendly.com"
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization",
            "Content-Disposition"
        ));
        
        // Permitir credenciales
        configuration.setAllowCredentials(true);
        
        // Tiempo de cache para preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Encoder de contraseñas (aunque usamos Supabase Auth, puede ser útil para otros casos).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
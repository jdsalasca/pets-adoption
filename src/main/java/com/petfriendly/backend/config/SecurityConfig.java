package com.petfriendly.backend.config;

import com.petfriendly.backend.security.JwtAuthenticationEntryPoint;
import com.petfriendly.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
 * Spring Security configuration with JWT authentication
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Security filter chain configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(AbstractHttpConfigurer::disable)
            
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure session management to be stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configure exception handling
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Ensure our DaoAuthenticationProvider is registered
            .authenticationProvider(authenticationProvider())
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/public/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/pets/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/foundations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/pet-images/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/contact-messages").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                
                // Swagger/OpenAPI endpoints
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Allow framework error endpoint so exceptions can surface without triggering auth
                .requestMatchers("/error").permitAll()
                
                // Health check endpoints
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                
                // User endpoints - authenticated users
                .requestMatchers(HttpMethod.GET, "/api/v1/users/profile").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/profile").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/profile").authenticated()
                
                // Adoption request endpoints - authenticated users
                .requestMatchers(HttpMethod.POST, "/api/v1/adoption-requests").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/adoption-requests/user/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/adoption-requests/*/cancel").authenticated()
                
                // Foundation management - foundation owners/admins
                .requestMatchers(HttpMethod.POST, "/api/v1/foundations").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/foundations/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/foundations/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                
                // Pet management - foundation owners/admins
                .requestMatchers(HttpMethod.POST, "/api/v1/pets").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/pets/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/pets/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                
                // Pet image management - foundation owners/admins
                .requestMatchers(HttpMethod.POST, "/api/v1/pet-images").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/pet-images/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/pet-images/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                
                // Adoption request management - foundation owners/admins
                .requestMatchers(HttpMethod.PUT, "/api/v1/adoption-requests/*/approve").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/adoption-requests/*/reject").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/adoption-requests/pet/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/adoption-requests/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                
                // Contact message management - foundation owners/admins
                .requestMatchers(HttpMethod.GET, "/api/v1/contact-messages/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/contact-messages/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/contact-messages/**").hasAnyRole("FOUNDATION_ADMIN", "ADMIN")
                
                // Admin only endpoints
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                
                // All other requests require authentication
                .anyRequest().authenticated()
            );

        // Add JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
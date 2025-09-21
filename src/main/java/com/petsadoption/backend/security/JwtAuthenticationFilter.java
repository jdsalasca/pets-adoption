package com.petsadoption.backend.security;

import com.petsadoption.backend.config.JwtConfig;
import com.petsadoption.backend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT que procesa tokens en cada request.
 * Extrae información del usuario del token JWT de Supabase y configura el contexto de seguridad.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final UUID userId;

        // Verificar si el header Authorization está presente y tiene el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT
        jwt = jwtConfig.extractTokenFromHeader(authHeader);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraer información del usuario del token
            userEmail = jwtConfig.extractEmail(jwt);
            userId = jwtConfig.extractUserId(jwt);

            // Si ya hay un usuario autenticado en el contexto, continuar
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Validar el token
                if (jwtConfig.validateToken(jwt)) {
                    
                    // Extraer roles del token
                    List<String> roles = jwtConfig.extractRoles(jwt);
                    String primaryRole = jwtConfig.extractRole(jwt);
                    
                    // Si no hay roles en el token, intentar obtenerlos de la base de datos
                    if ((roles == null || roles.isEmpty()) && primaryRole == null && userId != null) {
                        try {
                            var userEntity = userService.findById(userId);
                            if (userEntity.isPresent()) {
                                primaryRole = userEntity.get().getRole().name();
                                roles = List.of(primaryRole);
                            }
                        } catch (Exception e) {
                            log.warn("Could not fetch user roles from database for user {}: {}", userId, e.getMessage());
                        }
                    }

                    // Crear lista de authorities
                    List<SimpleGrantedAuthority> authorities = createAuthorities(roles, primaryRole);

                    // Crear token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            authorities
                    );

                    // Agregar detalles adicionales del request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Agregar información adicional del usuario al contexto
                    JwtUserDetails userDetails = JwtUserDetails.builder()
                            .id(userId)
                            .email(userEmail)
                            .role(primaryRole)
                            .roles(roles)
                            .build();

                    // Establecer el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // Agregar información del usuario como atributo del request
                    request.setAttribute("userDetails", userDetails);
                    request.setAttribute("userId", userId);
                    request.setAttribute("userEmail", userEmail);
                    request.setAttribute("userRole", primaryRole);

                    log.debug("Successfully authenticated user: {} with roles: {}", userEmail, authorities);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            // No establecer autenticación en caso de error, pero continuar con el filtro
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Crea la lista de authorities basada en los roles del usuario.
     */
    private List<SimpleGrantedAuthority> createAuthorities(List<String> roles, String primaryRole) {
        List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();

        // Agregar rol principal si existe
        if (primaryRole != null && !primaryRole.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + primaryRole));
        }

        // Agregar roles adicionales si existen
        if (roles != null && !roles.isEmpty()) {
            authorities.addAll(roles.stream()
                    .filter(role -> role != null && !role.isEmpty())
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList()));
        }

        // Si no hay roles, agregar rol por defecto
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }

    /**
     * Clase para encapsular detalles del usuario extraídos del JWT.
     */
    public static class JwtUserDetails {
        private UUID id;
        private String email;
        private String role;
        private List<String> roles;

        // Constructor por defecto
        public JwtUserDetails() {}

        // Constructor con parámetros
        public JwtUserDetails(UUID id, String email, String role, List<String> roles) {
            this.id = id;
            this.email = email;
            this.role = role;
            this.roles = roles;
        }

        // Getters y setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }

        // Método builder manual
        public static JwtUserDetailsBuilder builder() {
            return new JwtUserDetailsBuilder();
        }

        public static class JwtUserDetailsBuilder {
            private UUID id;
            private String email;
            private String role;
            private List<String> roles;

            public JwtUserDetailsBuilder id(UUID id) {
                this.id = id;
                return this;
            }

            public JwtUserDetailsBuilder email(String email) {
                this.email = email;
                return this;
            }

            public JwtUserDetailsBuilder role(String role) {
                this.role = role;
                return this;
            }

            public JwtUserDetailsBuilder roles(List<String> roles) {
                this.roles = roles;
                return this;
            }

            public JwtUserDetails build() {
                return new JwtUserDetails(id, email, role, roles);
            }
        }
    }
}
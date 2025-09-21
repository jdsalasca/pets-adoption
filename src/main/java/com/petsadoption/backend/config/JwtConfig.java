package com.petsadoption.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Configuración y utilidades para manejo de JWT tokens de Supabase.
 * Esta clase se encarga de validar y extraer información de los tokens JWT
 * generados por Supabase Auth.
 */
@Component
public class JwtConfig {

    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Obtiene la clave secreta para firmar/verificar JWT.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrae el username (email) del token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el user ID del token JWT.
     */
    public UUID extractUserId(String token) {
        String subject = extractClaim(token, Claims::getSubject);
        try {
            return UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            log.error("Error parsing user ID from JWT subject: {}", subject, e);
            return null;
        }
    }

    /**
     * Extrae el email del token JWT.
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Extrae el rol del token JWT.
     * Supabase puede incluir roles personalizados en el token mediante hooks.
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extrae los roles del token JWT como lista.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List) {
                return (List<String>) rolesObj;
            }
            return List.of();
        });
    }

    /**
     * Extrae metadatos del usuario del token JWT.
     */
    @SuppressWarnings("unchecked")
    public Object extractUserMetadata(String token, String key) {
        return extractClaim(token, claims -> {
            Object userMetadata = claims.get("user_metadata");
            if (userMetadata instanceof java.util.Map) {
                return ((java.util.Map<String, Object>) userMetadata).get(key);
            }
            return null;
        });
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token.
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Verifica si el token ha expirado.
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Valida el token JWT.
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida el token JWT sin verificar el username.
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Error validating JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrae el token del header Authorization.
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     */
    public boolean hasRole(String token, String role) {
        try {
            String userRole = extractRole(token);
            if (userRole != null && userRole.equals(role)) {
                return true;
            }
            
            List<String> roles = extractRoles(token);
            return roles.contains(role);
        } catch (Exception e) {
            log.error("Error checking user role: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el usuario tiene alguno de los roles especificados.
     */
    public boolean hasAnyRole(String token, String... roles) {
        for (String role : roles) {
            if (hasRole(token, role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extrae información completa del usuario del token.
     */
    public UserInfo extractUserInfo(String token) {
        try {
            return UserInfo.builder()
                    .id(extractUserId(token))
                    .email(extractEmail(token))
                    .role(extractRole(token))
                    .roles(extractRoles(token))
                    .build();
        } catch (Exception e) {
            log.error("Error extracting user info from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Clase para encapsular información del usuario extraída del JWT.
     */
    public static class UserInfo {
        private UUID id;
        private String email;
        private String role;
        private List<String> roles;

        // Constructor por defecto
        public UserInfo() {}

        // Constructor con parámetros
        public UserInfo(UUID id, String email, String role, List<String> roles) {
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
        public static UserInfoBuilder builder() {
            return new UserInfoBuilder();
        }

        public static class UserInfoBuilder {
            private UUID id;
            private String email;
            private String role;
            private List<String> roles;

            public UserInfoBuilder id(UUID id) {
                this.id = id;
                return this;
            }

            public UserInfoBuilder email(String email) {
                this.email = email;
                return this;
            }

            public UserInfoBuilder role(String role) {
                this.role = role;
                return this;
            }

            public UserInfoBuilder roles(List<String> roles) {
                this.roles = roles;
                return this;
            }

            public UserInfo build() {
                return new UserInfo(id, email, role, roles);
            }
        }
    }
}
package com.petfriendly.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT Authentication Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT authentication response")
public class JwtAuthenticationResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String accessToken;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType;
}
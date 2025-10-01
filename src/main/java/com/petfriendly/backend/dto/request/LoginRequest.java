package com.petfriendly.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request payload")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email", example = "user@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "User password", example = "password123")
    private String password;

    // Manual getters to ensure availability
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
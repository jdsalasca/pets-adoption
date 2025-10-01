package com.petfriendly.backend.entity;

import com.petfriendly.backend.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Entity.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_role", columnList = "role"),
        @Index(name = "idx_user_active", columnList = "active")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User email", example = "john.doe@example.com")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "User password (encrypted)")
    private String password;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User first name", example = "John")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "User role", example = "USER")
    private Role role;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    @Schema(description = "User phone number", example = "+1234567890")
    private String phone;

    @Size(max = 100, message = "City must not exceed 100 characters")
    @Schema(description = "User city", example = "Bogotá")
    private String city;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    @Schema(description = "User active status", example = "true")
    private Boolean active = true;

    /**
     * Returns whether the user is active.
     *
     * @return true if the user is active, false otherwise
     */
    public boolean isActive() {
        return active != null && active;
    }
}
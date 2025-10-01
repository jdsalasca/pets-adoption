package com.petfriendly.backend.dto.response;

import com.petfriendly.backend.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "User response payload")
public record UserResponse(
        @Schema(description = "User identifier") UUID id,
        @Schema(description = "First name") String firstName,
        @Schema(description = "Last name") String lastName,
        @Schema(description = "Email address") String email,
        @Schema(description = "Phone number") String phone,
        @Schema(description = "City") String city,
        @Schema(description = "Assigned role") Role role,
        @Schema(description = "Active flag") boolean active,
        @Schema(description = "Creation timestamp") LocalDateTime createdAt,
        @Schema(description = "Last update timestamp") LocalDateTime updatedAt
) {}

package com.petfriendly.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Foundation response payload")
public record FoundationResponse(
        UUID id,
        String name,
        String city,
        String state,
        String description,
        String contactEmail,
        String website,
        String address,
        String phoneNumber,
        boolean verified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

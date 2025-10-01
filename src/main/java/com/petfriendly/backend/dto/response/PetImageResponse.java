package com.petfriendly.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Pet image response payload")
public record PetImageResponse(
        UUID id,
        String imageUrl,
        boolean isPrimary,
        String altText,
        UUID petId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

package com.petfriendly.backend.dto.response;

import com.petfriendly.backend.entity.AdoptionRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Adoption request response payload")
public record AdoptionRequestResponse(
        UUID id,
        UUID userId,
        UUID petId,
        String message,
        String experience,
        String livingSituation,
        String reviewNotes,
        AdoptionRequestStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime reviewedAt
) {}

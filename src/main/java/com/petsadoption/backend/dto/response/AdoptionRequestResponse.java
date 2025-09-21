package com.petsadoption.backend.dto.response;

import com.petsadoption.backend.entity.AdoptionRequest;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Respuesta serializada de solicitudes de adopción.
 */
public record AdoptionRequestResponse(
        UUID id,
        UUID petId,
        UUID foundationId,
        UUID userId,
        AdoptionRequest.Status status,
        String answersJson,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdoptionRequestResponse fromEntity(AdoptionRequest request) {
        UUID petId = request.getPet() != null ? request.getPet().getId() : null;
        UUID foundationId = request.getPet() != null && request.getPet().getFoundation() != null
                ? request.getPet().getFoundation().getId() : null;
        UUID userId = request.getUser() != null ? request.getUser().getId() : null;

        return new AdoptionRequestResponse(
                request.getId(),
                petId,
                foundationId,
                userId,
                request.getStatus(),
                request.getAnswersJson(),
                request.getNotes(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}

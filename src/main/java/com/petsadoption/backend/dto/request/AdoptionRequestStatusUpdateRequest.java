package com.petsadoption.backend.dto.request;

import com.petsadoption.backend.entity.AdoptionRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Payload para actualizar el estado de una solicitud de adopción.
 */
public record AdoptionRequestStatusUpdateRequest(
        @NotNull(message = "El estado es obligatorio")
        AdoptionRequest.Status status,

        @Size(max = 2000, message = "Las notas no pueden exceder 2000 caracteres")
        String notes
) {
}

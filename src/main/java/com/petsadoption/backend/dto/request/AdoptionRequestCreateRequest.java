package com.petsadoption.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Payload para crear una solicitud de adopción.
 */
public record AdoptionRequestCreateRequest(
        @NotNull(message = "El identificador de la mascota es obligatorio")
        UUID petId,

        @NotBlank(message = "Debes responder al formulario")
        String answersJson,

        @Size(max = 2000, message = "Las notas adicionales no pueden exceder 2000 caracteres")
        String notes
) {
}

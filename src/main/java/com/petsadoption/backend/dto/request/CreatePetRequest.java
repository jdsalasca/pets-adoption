package com.petsadoption.backend.dto.request;

import com.petsadoption.backend.entity.Pet;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Payload para registrar una nueva mascota desde el panel de la fundación.
 * El diseño sigue el modelo de datos del MVP definido para PETFRIENDLY.
 */
public record CreatePetRequest(
        @NotBlank(message = "El nombre de la mascota es obligatorio")
        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        String name,

        @NotNull(message = "La especie es obligatoria")
        Pet.Species species,

        @NotBlank(message = "La raza es obligatoria")
        @Size(max = 255, message = "La raza no puede exceder 255 caracteres")
        String breed,

        @NotNull(message = "El sexo es obligatorio")
        Pet.Sex sex,

        @NotNull(message = "La edad en meses es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 600, message = "La edad no puede exceder 600 meses (50 años)")
        Integer ageMonths,

        @NotNull(message = "El tamaño es obligatorio")
        Pet.Size size,

        @Size(max = 255, message = "El temperamento no puede exceder 255 caracteres")
        String temperament,

        @Size(max = 2000, message = "La información de salud no puede exceder 2000 caracteres")
        String health,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
        String city,

        @Size(max = 4000, message = "La descripción no puede exceder 4000 caracteres")
        String description,

        @NotNull(message = "El identificador de la fundación es obligatorio")
        UUID foundationId
) {
}

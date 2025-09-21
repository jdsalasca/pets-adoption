package com.petsadoption.backend.dto;

import com.petsadoption.backend.entity.AdoptionRequest;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para transferencia de datos de solicitudes de adopción.
 * Utilizado para comunicación entre el frontend y backend.
 */
public record AdoptionRequestDTO(
        UUID id,

        UUID petId,

        String petName,

        @NotBlank(message = "El nombre del solicitante es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String applicantName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,20}$", message = "El teléfono debe tener un formato válido")
        String phone,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 300, message = "La dirección no puede exceder 300 caracteres")
        String address,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 18, message = "Debe ser mayor de edad para adoptar")
        @Max(value = 120, message = "La edad no puede exceder 120 años")
        Integer age,

        @NotNull(message = "El tipo de vivienda es obligatorio")
        String housingType,

        @NotNull(message = "Debe especificar si tiene patio")
        Boolean hasYard,

        @NotNull(message = "Debe especificar si tiene otras mascotas")
        Boolean hasOtherPets,

        String otherPetsDescription,

        @NotNull(message = "Debe especificar si tiene experiencia con mascotas")
        Boolean hasPetExperience,

        String petExperienceDescription,

        @NotBlank(message = "La razón para adoptar es obligatoria")
        @Size(min = 10, max = 1000, message = "La razón debe tener entre 10 y 1000 caracteres")
        String reasonForAdoption,

        String additionalComments,

        @NotNull(message = "El estado de la solicitud es obligatorio")
        AdoptionRequest.Status status,

        String adminComments,

        LocalDateTime reviewedAt,

        String reviewedBy,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {

    /**
     * Constructor para crear un AdoptionRequestDTO desde una entidad AdoptionRequest.
     * @param request entidad AdoptionRequest
     * @return AdoptionRequestDTO
     */
    public static AdoptionRequestDTO fromEntity(AdoptionRequest request) {
        return new AdoptionRequestDTO(
                request.getId(),
                request.getPet() != null ? request.getPet().getId() : null,
                request.getPet() != null ? request.getPet().getName() : null,
                request.getUser() != null ? (request.getUser().getFirstName() + " " + request.getUser().getLastName()) : null,
                request.getUser() != null ? request.getUser().getEmail() : null,
                request.getUser() != null ? request.getUser().getPhone() : null,
                null, // address - no disponible en User
                null, // age - no disponible en User
                null, // housingType - no disponible
                null, // hasYard - no disponible
                null, // hasOtherPets - no disponible
                null, // otherPetsDescription - no disponible
                null, // hasPetExperience - no disponible
                null, // petExperienceDescription - no disponible
                null, // reasonForAdoption - no disponible
                null, // additionalComments - no disponible
                request.getStatus(),
                request.getNotes(),
                request.getReviewedAt(),
                request.getReviewedBy() != null ? request.getReviewedBy().toString() : null,
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }

    /**
     * DTO simplificado para listados de solicitudes.
     */
    public record AdoptionRequestSummaryDTO(
            UUID id,
            UUID petId,
            String petName,
            String applicantName,
            String email,
            AdoptionRequest.Status status,
            LocalDateTime createdAt,
            LocalDateTime reviewedAt
    ) {
        public static AdoptionRequestSummaryDTO fromEntity(AdoptionRequest request) {
            return new AdoptionRequestSummaryDTO(
                    request.getId(),
                    request.getPet() != null ? request.getPet().getId() : null,
                    request.getPet() != null ? request.getPet().getName() : null,
                    request.getUser() != null ? (request.getUser().getFirstName() + " " + request.getUser().getLastName()) : null,
                    request.getUser() != null ? request.getUser().getEmail() : null,
                    request.getStatus(),
                    request.getCreatedAt(),
                    request.getReviewedAt()
            );
        }
    }

    /**
     * DTO para crear una nueva solicitud de adopción.
     */
    public record CreateAdoptionRequestDTO(
            @NotNull(message = "El ID de la mascota es obligatorio")
            Long petId,

            @NotBlank(message = "El nombre del solicitante es obligatorio")
            @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
            String applicantName,

            @NotBlank(message = "El email es obligatorio")
            @Email(message = "El email debe tener un formato válido")
            @Size(max = 150, message = "El email no puede exceder 150 caracteres")
            String email,

            @NotBlank(message = "El teléfono es obligatorio")
            @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,20}$", message = "El teléfono debe tener un formato válido")
            String phone,

            @NotBlank(message = "La dirección es obligatoria")
            @Size(max = 300, message = "La dirección no puede exceder 300 caracteres")
            String address,

            @NotNull(message = "La edad es obligatoria")
            @Min(value = 18, message = "Debe ser mayor de edad para adoptar")
            @Max(value = 120, message = "La edad no puede exceder 120 años")
            Integer age,

            @NotNull(message = "El tipo de vivienda es obligatorio")
            AdoptionRequest.HousingType housingType,

            @NotNull(message = "Debe especificar si tiene patio")
            Boolean hasYard,

            @NotNull(message = "Debe especificar si tiene otras mascotas")
            Boolean hasOtherPets,

            String otherPetsDescription,

            @NotNull(message = "Debe especificar si tiene experiencia con mascotas")
            Boolean hasPetExperience,

            String petExperienceDescription,

            @NotBlank(message = "La razón para adoptar es obligatoria")
            @Size(min = 10, max = 1000, message = "La razón debe tener entre 10 y 1000 caracteres")
            String reasonForAdoption,

            String additionalComments
    ) {}

    /**
     * DTO para actualizar el estado de una solicitud (uso administrativo).
     */
    public record UpdateAdoptionRequestStatusDTO(
            @NotNull(message = "El estado es obligatorio")
            AdoptionRequest.Status status,

            String adminComments,

            String reviewedBy
    ) {}

    /**
     * DTO para estadísticas de solicitudes.
     */
    public record AdoptionRequestStatsDTO(
            long totalRequests,
            long pendingRequests,
            long approvedRequests,
            long rejectedRequests,
            long completedRequests,
            double approvalRate
    ) {}
}
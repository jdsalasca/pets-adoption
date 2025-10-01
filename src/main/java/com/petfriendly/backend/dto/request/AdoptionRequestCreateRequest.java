package com.petfriendly.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Adoption request creation payload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create adoption request payload")
public class AdoptionRequestCreateRequest {

    @NotNull(message = "Pet id is required")
    @Schema(description = "Pet identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID petId;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    @Schema(description = "Applicant message", example = "We have a large backyard and experience with dogs.")
    private String message;

    @Size(max = 2000, message = "Experience must not exceed 2000 characters")
    @Schema(description = "Previous pet experience")
    private String experience;

    @Size(max = 2000, message = "Living situation must not exceed 2000 characters")
    @Schema(description = "Applicant living situation")
    private String livingSituation;
}

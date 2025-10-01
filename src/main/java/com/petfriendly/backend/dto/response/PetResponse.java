package com.petfriendly.backend.dto.response;

import com.petfriendly.backend.entity.PetGender;
import com.petfriendly.backend.entity.PetSize;
import com.petfriendly.backend.entity.PetSpecies;
import com.petfriendly.backend.entity.PetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Pet response payload")
public record PetResponse(
        UUID id,
        String name,
        PetSpecies species,
        String breed,
        Integer age,
        PetGender gender,
        PetSize size,
        String description,
        PetStatus status,
        UUID foundationId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}

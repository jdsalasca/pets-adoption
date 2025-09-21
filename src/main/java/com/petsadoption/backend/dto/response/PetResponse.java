package com.petsadoption.backend.dto.response;

import com.petsadoption.backend.entity.Foundation;
import com.petsadoption.backend.entity.Pet;
import com.petsadoption.backend.entity.PetImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Respuesta canónica para exponer mascotas en la API pública.
 */
public record PetResponse(
        UUID id,
        UUID foundationId,
        String foundationName,
        String name,
        Pet.Species species,
        String breed,
        Pet.Sex sex,
        Integer ageMonths,
        Pet.Size size,
        String temperament,
        String health,
        String city,
        Pet.Status status,
        String description,
        List<PetImageResponse> images,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PetResponse fromEntity(Pet pet) {
        Foundation foundation = pet.getFoundation();
        List<PetImageResponse> imageResponses = pet.getImages() == null ? List.of() :
                pet.getImages().stream()
                        .sorted((a, b) -> Integer.compare(
                                a.getOrderIndex() == null ? Integer.MAX_VALUE : a.getOrderIndex(),
                                b.getOrderIndex() == null ? Integer.MAX_VALUE : b.getOrderIndex()))
                        .map(PetImageResponse::fromEntity)
                        .toList();

        return new PetResponse(
                pet.getId(),
                foundation != null ? foundation.getId() : null,
                foundation != null ? foundation.getName() : null,
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getSex(),
                pet.getAgeMonths(),
                pet.getSize(),
                pet.getTemperament(),
                pet.getHealth(),
                pet.getCity(),
                pet.getStatus(),
                pet.getDescription(),
                imageResponses,
                pet.getCreatedAt(),
                pet.getUpdatedAt()
        );
    }

    public record PetImageResponse(
            UUID id,
            String s3Key,
            Integer orderIndex,
            String altText,
            boolean primary
    ) {
        static PetImageResponse fromEntity(PetImage image) {
            return new PetImageResponse(
                    image.getId(),
                    image.getS3Key(),
                    image.getOrderIndex(),
                    image.getAltText(),
                    Boolean.TRUE.equals(image.getIsPrimary())
            );
        }
    }
}

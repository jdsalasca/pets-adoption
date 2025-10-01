package com.petfriendly.backend.controller;

import com.petfriendly.backend.dto.mapper.DtoMapper;
import com.petfriendly.backend.dto.response.PetImageResponse;
import com.petfriendly.backend.entity.PetImage;
import com.petfriendly.backend.service.PetImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for PetImage entity operations
 */
@RestController
@RequestMapping("/api/v1/pet-images")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PetImageController {

    // Manual logger since Lombok @Slf4j is not working
    private static final Logger log = LoggerFactory.getLogger(PetImageController.class);
    
    private final PetImageService petImageService;

    private PetImageResponse toResponse(PetImage petImage) {
        return DtoMapper.toPetImageResponse(petImage);
    }

    private List<PetImageResponse> toResponses(List<PetImage> images) {
        return DtoMapper.toPetImageResponses(images);
    }

    private Page<PetImageResponse> toResponsePage(Page<PetImage> images) {
        return DtoMapper.mapPage(images, DtoMapper::toPetImageResponse);
    }

    /**
     * Create a new pet image
     * POST /api/v1/pet-images
     */
    @PostMapping
    @Operation(summary = "Create pet image", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetImageResponse> createPetImage(@Valid @RequestBody PetImage petImage) {
        log.info("Creating new pet image for pet ID: {}", petImage.getPet().getId());
        PetImage createdPetImage = petImageService.createPetImage(petImage);
        return new ResponseEntity<>(toResponse(createdPetImage), HttpStatus.CREATED);
    }

    /**
     * Get all pet images
     * GET /api/v1/pet-images
     */
    @GetMapping
    public ResponseEntity<List<PetImageResponse>> getAllPetImages() {
        log.info("Getting all pet images");
        return ResponseEntity.ok(toResponses(petImageService.findAll()));
    }

    /**
     * Get all pet images with pagination
     * GET /api/v1/pet-images/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<PetImageResponse>> getAllPetImagesWithPagination(Pageable pageable) {
        log.info("Getting all pet images with pagination");
        return ResponseEntity.ok(toResponsePage(petImageService.findAll(pageable)));
    }

    /**
     * Get pet image by ID
     * GET /api/v1/pet-images/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetImageResponse> getPetImageById(@PathVariable UUID id) {
        log.info("Getting pet image by ID: {}", id);
        return petImageService.findById(id)
                .map(petImage -> ResponseEntity.ok(toResponse(petImage)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get pet images by pet ID
     * GET /api/v1/pet-images/pet/{petId}
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetImageResponse>> getPetImagesByPetId(@PathVariable UUID petId) {
        log.info("Getting pet images by pet ID: {}", petId);
        return ResponseEntity.ok(toResponses(petImageService.findByPetId(petId)));
    }

    /**
     * Get pet images by pet ID with pagination
     * GET /api/v1/pet-images/pet/{petId}/page
     */
    @GetMapping("/pet/{petId}/page")
    public ResponseEntity<Page<PetImageResponse>> getPetImagesByPetIdWithPagination(@PathVariable UUID petId, Pageable pageable) {
        log.info("Getting pet images by pet ID with pagination: {}", petId);
        return ResponseEntity.ok(toResponsePage(petImageService.findByPetId(petId, pageable)));
    }

    /**
     * Get primary image for a pet
     * GET /api/v1/pet-images/pet/{petId}/primary
     */
    @GetMapping("/pet/{petId}/primary")
    public ResponseEntity<PetImageResponse> getPrimaryImageByPetId(@PathVariable UUID petId) {
        log.info("Getting primary image for pet ID: {}", petId);
        return petImageService.findPrimaryByPetId(petId)
                .map(petImage -> ResponseEntity.ok(toResponse(petImage)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get non-primary images for a pet
     * GET /api/v1/pet-images/pet/{petId}/secondary
     */
    @GetMapping("/pet/{petId}/secondary")
    public ResponseEntity<List<PetImageResponse>> getSecondaryImagesByPetId(@PathVariable UUID petId) {
        log.info("Getting secondary images for pet ID: {}", petId);
        return ResponseEntity.ok(toResponses(petImageService.findSecondaryByPetId(petId)));
    }

    /**
     * Get non-primary images for a pet with pagination
     * GET /api/v1/pet-images/pet/{petId}/secondary/page
     */
    @GetMapping("/pet/{petId}/secondary/page")
    public ResponseEntity<Page<PetImageResponse>> getSecondaryImagesByPetIdWithPagination(@PathVariable UUID petId, Pageable pageable) {
        log.info("Getting secondary images for pet ID with pagination: {}", petId);
        return ResponseEntity.ok(toResponsePage(petImageService.findSecondaryByPetId(petId, pageable)));
    }

    /**
     * Get pet images by URL pattern
     * GET /api/v1/pet-images/search?url={urlPattern}
     */
    @GetMapping("/search")
    public ResponseEntity<List<PetImageResponse>> searchPetImagesByUrl(@RequestParam String url) {
        log.info("Searching pet images by URL pattern: {}", url);
        return ResponseEntity.ok(toResponses(petImageService.findByImageUrlContaining(url)));
    }

    /**
     * Get pet images by URL pattern with pagination
     * GET /api/v1/pet-images/search/page?url={urlPattern}
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<PetImageResponse>> searchPetImagesByUrlWithPagination(@RequestParam String url, Pageable pageable) {
        log.info("Searching pet images by URL pattern with pagination: {}", url);
        return ResponseEntity.ok(toResponsePage(petImageService.findByImageUrlContaining(url, pageable)));
    }

    /**
     * Update pet image
     * PUT /api/v1/pet-images/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update pet image", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetImageResponse> updatePetImage(@PathVariable UUID id, @Valid @RequestBody PetImage petImage) {
        log.info("Updating pet image with ID: {}", id);
        try {
            PetImage updatedPetImage = petImageService.updatePetImage(id, petImage);
            return ResponseEntity.ok(toResponse(updatedPetImage));
        } catch (RuntimeException e) {
            log.error("Error updating pet image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Set image as primary for a pet
     * PUT /api/v1/pet-images/{id}/set-primary
     */
    @PutMapping("/{id}/set-primary")
    @Operation(summary = "Set primary image", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetImageResponse> setPrimaryImage(@PathVariable UUID id) {
        log.info("Setting pet image as primary: {}", id);
        try {
            PetImage primaryImage = petImageService.setPrimaryImage(id);
            return ResponseEntity.ok(toResponse(primaryImage));
        } catch (RuntimeException e) {
            log.error("Error setting pet image as primary: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove primary status from image
     * PUT /api/v1/pet-images/{id}/remove-primary
     */
    @PutMapping("/{id}/remove-primary")
    @Operation(summary = "Remove primary flag", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetImageResponse> removePrimaryStatus(@PathVariable UUID id) {
        log.info("Removing primary status from pet image: {}", id);
        try {
            PetImage updatedImage = petImageService.removePrimaryStatus(id);
            return ResponseEntity.ok(toResponse(updatedImage));
        } catch (RuntimeException e) {
            log.error("Error removing primary status from pet image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete pet image
     * DELETE /api/v1/pet-images/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pet image", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deletePetImage(@PathVariable UUID id) {
        log.info("Deleting pet image with ID: {}", id);
        try {
            petImageService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting pet image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete all images for a pet
     * DELETE /api/v1/pet-images/pet/{petId}
     */
    @DeleteMapping("/pet/{petId}")
    @Operation(summary = "Delete images for pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteAllPetImages(@PathVariable UUID petId) {
        log.info("Deleting all images for pet ID: {}", petId);
        try {
            petImageService.deleteByPetId(petId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting pet images: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Check if pet image exists by ID
     * HEAD /api/v1/pet-images/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkPetImageExists(@PathVariable UUID id) {
        log.info("Checking if pet image exists with ID: {}", id);
        if (petImageService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if pet has primary image
     * HEAD /api/v1/pet-images/pet/{petId}/has-primary
     */
    @RequestMapping(value = "/pet/{petId}/has-primary", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkPetHasPrimaryImage(@PathVariable UUID petId) {
        log.info("Checking if pet has primary image: {}", petId);
        if (petImageService.hasPrimaryImage(petId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get pet image count
     * GET /api/v1/pet-images/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getPetImageCount() {
        log.info("Getting total pet image count");
        long count = petImageService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet image count by pet
     * GET /api/v1/pet-images/count/pet/{petId}
     */
    @GetMapping("/count/pet/{petId}")
    public ResponseEntity<Long> getPetImageCountByPet(@PathVariable UUID petId) {
        log.info("Getting pet image count by pet: {}", petId);
        long count = petImageService.countByPetId(petId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet image statistics
     * GET /api/v1/pet-images/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<PetImageService.PetImageStatistics> getPetImageStatistics() {
        log.info("Getting pet image statistics");
        PetImageService.PetImageStatistics statistics = petImageService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get pet image statistics by pet
     * GET /api/v1/pet-images/statistics/pet/{petId}
     */
    @GetMapping("/statistics/pet/{petId}")
    public ResponseEntity<PetImageService.PetImageStatistics> getPetImageStatisticsByPet(@PathVariable UUID petId) {
        log.info("Getting pet image statistics by pet: {}", petId);
        PetImageService.PetImageStatistics statistics = petImageService.getStatisticsByPet(petId);
        return ResponseEntity.ok(statistics);
    }
}

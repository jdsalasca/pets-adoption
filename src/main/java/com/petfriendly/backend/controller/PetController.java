package com.petfriendly.backend.controller;

import com.petfriendly.backend.dto.mapper.DtoMapper;
import com.petfriendly.backend.dto.response.PetResponse;
import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetSpecies;
import com.petfriendly.backend.entity.PetStatus;
import com.petfriendly.backend.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Pet entity operations
 */
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    private PetResponse toResponse(Pet pet) {
        return DtoMapper.toPetResponse(pet);
    }

    private List<PetResponse> toResponses(List<Pet> pets) {
        return DtoMapper.toPetResponses(pets);
    }

    private Page<PetResponse> toResponsePage(Page<Pet> pets) {
        return DtoMapper.mapPage(pets, DtoMapper::toPetResponse);
    }

    /**
     * Create a new pet
     * POST /api/v1/pets
     */
    @PostMapping
    @Operation(summary = "Create a pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody Pet pet) {
        log.info("Creating new pet: {}", pet.getName());
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(toResponse(createdPet), HttpStatus.CREATED);
    }

    /**
     * Get all pets with pagination
     * GET /api/v1/pets
     */
    @GetMapping
    public ResponseEntity<Page<PetResponse>> getAllPets(Pageable pageable) {
        log.info("Getting all pets with pagination");
        return ResponseEntity.ok(toResponsePage(petService.findAll(pageable)));
    }

    /**
     * Get all pets with pagination
     * GET /api/v1/pets/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<PetResponse>> getAllPetsWithPagination(Pageable pageable) {
        log.info("Getting all pets with pagination");
        return ResponseEntity.ok(toResponsePage(petService.findAll(pageable)));
    }

    /**
     * Get pet by ID
     * GET /api/v1/pets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable UUID id) {
        log.info("Getting pet by ID: {}", id);
        return petService.findById(id)
                .map(pet -> ResponseEntity.ok(toResponse(pet)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get pets by foundation ID
     * GET /api/v1/pets/foundation/{foundationId}
     */
    @GetMapping("/foundation/{foundationId}")
    public ResponseEntity<List<PetResponse>> getPetsByFoundationId(@PathVariable UUID foundationId) {
        log.info("Getting pets by foundation ID: {}", foundationId);
        return ResponseEntity.ok(toResponses(petService.findByFoundationId(foundationId)));
    }

    /**
     * Get pets by foundation ID with pagination
     * GET /api/v1/pets/foundation/{foundationId}/page
     */
    @GetMapping("/foundation/{foundationId}/page")
    public ResponseEntity<Page<PetResponse>> getPetsByFoundationIdWithPagination(@PathVariable UUID foundationId, Pageable pageable) {
        log.info("Getting pets by foundation ID with pagination: {}", foundationId);
        return ResponseEntity.ok(toResponsePage(petService.findByFoundationId(foundationId, pageable)));
    }

    /**
     * Get pets by status
     * GET /api/v1/pets/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PetResponse>> getPetsByStatus(@PathVariable PetStatus status) {
        log.info("Getting pets by status: {}", status);
        return ResponseEntity.ok(toResponses(petService.findByStatus(status)));
    }

    /**
     * Get pets by status with pagination
     * GET /api/v1/pets/status/{status}/page
     */
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<PetResponse>> getPetsByStatusWithPagination(@PathVariable PetStatus status, Pageable pageable) {
        log.info("Getting pets by status with pagination: {}", status);
        return ResponseEntity.ok(toResponsePage(petService.findByStatus(status, pageable)));
    }

    /**
     * Get pets by species
     * GET /api/v1/pets/species/{species}
     */
    @GetMapping("/species/{species}")
    public ResponseEntity<List<PetResponse>> getPetsBySpecies(@PathVariable String species) {
        log.info("Getting pets by species: {}", species);
        PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
        return ResponseEntity.ok(toResponses(petService.findBySpecies(petSpecies)));
    }

    /**
     * Get pets by species with pagination
     * GET /api/v1/pets/species/{species}/page
     */
    @GetMapping("/species/{species}/page")
    public ResponseEntity<Page<PetResponse>> getPetsBySpeciesWithPagination(@PathVariable String species, Pageable pageable) {
        log.info("Getting pets by species with pagination: {}", species);
        PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
        return ResponseEntity.ok(toResponsePage(petService.findBySpecies(petSpecies, pageable)));
    }

    /**
     * Get pets by breed
     * GET /api/v1/pets/breed/{breed}
     */
    @GetMapping("/breed/{breed}")
    public ResponseEntity<List<PetResponse>> getPetsByBreed(@PathVariable String breed) {
        log.info("Getting pets by breed: {}", breed);
        return ResponseEntity.ok(toResponses(petService.findByBreed(breed)));
    }

    /**
     * Get pets by breed with pagination
     * GET /api/v1/pets/breed/{breed}/page
     */
    @GetMapping("/breed/{breed}/page")
    public ResponseEntity<Page<PetResponse>> getPetsByBreedWithPagination(@PathVariable String breed, Pageable pageable) {
        log.info("Getting pets by breed with pagination: {}", breed);
        return ResponseEntity.ok(toResponsePage(petService.findByBreed(breed, pageable)));
    }

    /**
     * Get pets by age range
     * GET /api/v1/pets/age?min={minAge}&max={maxAge}
     */
    @GetMapping("/age")
    public ResponseEntity<List<PetResponse>> getPetsByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        log.info("Getting pets by age range: {} to {}", minAge, maxAge);
        return ResponseEntity.ok(toResponses(petService.findByAgeBetween(minAge, maxAge)));
    }

    /**
     * Get pets by age range with pagination
     * GET /api/v1/pets/age/page?min={minAge}&max={maxAge}
     */
    @GetMapping("/age/page")
    public ResponseEntity<Page<PetResponse>> getPetsByAgeRangeWithPagination(@RequestParam Integer minAge, @RequestParam Integer maxAge, Pageable pageable) {
        log.info("Getting pets by age range with pagination: {} to {}", minAge, maxAge);
        return ResponseEntity.ok(toResponsePage(petService.findByAgeBetween(minAge, maxAge, pageable)));
    }

    /**
     * Get available pets for adoption
     * GET /api/v1/pets/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<PetResponse>> getAvailablePets() {
        log.info("Getting available pets for adoption");
        return ResponseEntity.ok(toResponses(petService.findAvailableForAdoption()));
    }

    /**
     * Get available pets for adoption with pagination
     * GET /api/v1/pets/available/page
     */
    @GetMapping("/available/page")
    public ResponseEntity<Page<PetResponse>> getAvailablePetsWithPagination(Pageable pageable) {
        log.info("Getting available pets for adoption with pagination");
        return ResponseEntity.ok(toResponsePage(petService.findAvailableForAdoption(pageable)));
    }

    /**
     * Search pets by name
     * GET /api/v1/pets/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<PetResponse>> searchPetsByName(@RequestParam String name) {
        log.info("Searching pets by name: {}", name);
        return ResponseEntity.ok(toResponses(petService.findByNameContaining(name)));
    }

    /**
     * Search pets by name with pagination
     * GET /api/v1/pets/search/page?name={name}
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<PetResponse>> searchPetsByNameWithPagination(@RequestParam String name, Pageable pageable) {
        log.info("Searching pets by name with pagination: {}", name);
        return ResponseEntity.ok(toResponsePage(petService.findByNameContaining(name, pageable)));
    }

    /**
     * Get pets by foundation and status
     * GET /api/v1/pets/foundation/{foundationId}/status/{status}
     */
    @GetMapping("/foundation/{foundationId}/status/{status}")
    public ResponseEntity<List<PetResponse>> getPetsByFoundationAndStatus(@PathVariable UUID foundationId, @PathVariable PetStatus status) {
        log.info("Getting pets by foundation ID {} and status: {}", foundationId, status);
        return ResponseEntity.ok(toResponses(petService.findByFoundationIdAndStatus(foundationId, status)));
    }

    /**
     * Get pets by foundation and status with pagination
     * GET /api/v1/pets/foundation/{foundationId}/status/{status}/page
     */
    @GetMapping("/foundation/{foundationId}/status/{status}/page")
    public ResponseEntity<Page<PetResponse>> getPetsByFoundationAndStatusWithPagination(@PathVariable UUID foundationId, @PathVariable PetStatus status, Pageable pageable) {
        log.info("Getting pets by foundation ID {} and status with pagination: {}", foundationId, status);
        return ResponseEntity.ok(toResponsePage(petService.findByFoundationIdAndStatus(foundationId, status, pageable)));
    }

    /**
     * Update pet
     * PUT /api/v1/pets/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetResponse> updatePet(@PathVariable UUID id, @Valid @RequestBody Pet pet) {
        log.info("Updating pet with ID: {}", id);
        try {
            Pet updatedPet = petService.updatePet(id, pet);
            return ResponseEntity.ok(toResponse(updatedPet));
        } catch (RuntimeException e) {
            log.error("Error updating pet: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update pet status
     * PUT /api/v1/pets/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update pet status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetResponse> updatePetStatus(@PathVariable UUID id, @RequestBody PetStatus status) {
        log.info("Updating status for pet ID {} to: {}", id, status);
        try {
            Pet updatedPet = petService.updateStatus(id, status);
            return ResponseEntity.ok(toResponse(updatedPet));
        } catch (RuntimeException e) {
            log.error("Error updating pet status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mark pet as adopted
     * PUT /api/v1/pets/{id}/adopt
     */
    @PutMapping("/{id}/adopt")
    @Operation(summary = "Mark pet as adopted", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetResponse> adoptPet(@PathVariable UUID id) {
        log.info("Marking pet as adopted: {}", id);
        try {
            Pet adoptedPet = petService.markAsAdopted(id);
            return ResponseEntity.ok(toResponse(adoptedPet));
        } catch (RuntimeException e) {
            log.error("Error marking pet as adopted: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mark pet as available
     * PUT /api/v1/pets/{id}/available
     */
    @PutMapping("/{id}/available")
    @Operation(summary = "Mark pet as available", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PetResponse> markPetAsAvailable(@PathVariable UUID id) {
        log.info("Marking pet as available: {}", id);
        try {
            Pet availablePet = petService.markAsAvailable(id);
            return ResponseEntity.ok(toResponse(availablePet));
        } catch (RuntimeException e) {
            log.error("Error marking pet as available: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete pet
     * DELETE /api/v1/pets/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deletePet(@PathVariable UUID id) {
        log.info("Deleting pet with ID: {}", id);
        try {
            petService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting pet: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if pet exists by ID
     * HEAD /api/v1/pets/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkPetExists(@PathVariable UUID id) {
        log.info("Checking if pet exists with ID: {}", id);
        if (petService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get pet count
     * GET /api/v1/pets/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getPetCount() {
        log.info("Getting total pet count");
        long count = petService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet count by foundation
     * GET /api/v1/pets/count/foundation/{foundationId}
     */
    @GetMapping("/count/foundation/{foundationId}")
    public ResponseEntity<Long> getPetCountByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting pet count by foundation: {}", foundationId);
        long count = petService.countByFoundationId(foundationId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet count by status
     * GET /api/v1/pets/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getPetCountByStatus(@PathVariable PetStatus status) {
        log.info("Getting pet count by status: {}", status);
        long count = petService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet count by foundation and status
     * GET /api/v1/pets/count/foundation/{foundationId}/status/{status}
     */
    @GetMapping("/count/foundation/{foundationId}/status/{status}")
    public ResponseEntity<Long> getPetCountByFoundationAndStatus(@PathVariable UUID foundationId, @PathVariable PetStatus status) {
        log.info("Getting pet count by foundation {} and status: {}", foundationId, status);
        long count = petService.countByFoundationIdAndStatus(foundationId, status);
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet count by species
     * GET /api/v1/pets/count/species/{species}
     */
    @GetMapping("/count/species/{species}")
    public ResponseEntity<Long> getPetCountBySpecies(@PathVariable String species) {
        log.info("Getting pet count by species: {}", species);
        long count = petService.countBySpecies(species);
        return ResponseEntity.ok(count);
    }

    /**
     * Get pet statistics
     * GET /api/v1/pets/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<PetService.PetStatistics> getPetStatistics() {
        log.info("Getting pet statistics");
        PetService.PetStatistics statistics = petService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get pet statistics by foundation
     * GET /api/v1/pets/statistics/foundation/{foundationId}
     */
    @GetMapping("/statistics/foundation/{foundationId}")
    public ResponseEntity<PetService.PetStatistics> getPetStatisticsByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting pet statistics by foundation: {}", foundationId);
        PetService.PetStatistics statistics = petService.getStatisticsByFoundation(foundationId);
        return ResponseEntity.ok(statistics);
    }
}

package com.petfriendly.backend.controller;

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

    /**
     * Create a new pet
     * POST /api/v1/pets
     */
    @PostMapping
    @Operation(summary = "Create a pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Pet> createPet(@Valid @RequestBody Pet pet) {
        log.info("Creating new pet: {}", pet.getName());
        Pet createdPet = petService.createPet(pet);
        return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
    }

    /**
     * Get all pets with pagination
     * GET /api/v1/pets
     */
    @GetMapping
    public ResponseEntity<Page<Pet>> getAllPets(Pageable pageable) {
        log.info("Getting all pets with pagination");
        Page<Pet> pets = petService.findAll(pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get all pets with pagination
     * GET /api/v1/pets/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Pet>> getAllPetsWithPagination(Pageable pageable) {
        log.info("Getting all pets with pagination");
        Page<Pet> pets = petService.findAll(pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pet by ID
     * GET /api/v1/pets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        log.info("Getting pet by ID: {}", id);
        return petService.findById(id)
                .map(pet -> ResponseEntity.ok(pet))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get pets by foundation ID
     * GET /api/v1/pets/foundation/{foundationId}
     */
    @GetMapping("/foundation/{foundationId}")
    public ResponseEntity<List<Pet>> getPetsByFoundationId(@PathVariable UUID foundationId) {
        log.info("Getting pets by foundation ID: {}", foundationId);
        List<Pet> pets = petService.findByFoundationId(foundationId);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by foundation ID with pagination
     * GET /api/v1/pets/foundation/{foundationId}/page
     */
    @GetMapping("/foundation/{foundationId}/page")
    public ResponseEntity<Page<Pet>> getPetsByFoundationIdWithPagination(@PathVariable UUID foundationId, Pageable pageable) {
        log.info("Getting pets by foundation ID with pagination: {}", foundationId);
        Page<Pet> pets = petService.findByFoundationId(foundationId, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by status
     * GET /api/v1/pets/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pet>> getPetsByStatus(@PathVariable PetStatus status) {
        log.info("Getting pets by status: {}", status);
        List<Pet> pets = petService.findByStatus(status);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by status with pagination
     * GET /api/v1/pets/status/{status}/page
     */
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<Pet>> getPetsByStatusWithPagination(@PathVariable PetStatus status, Pageable pageable) {
        log.info("Getting pets by status with pagination: {}", status);
        Page<Pet> pets = petService.findByStatus(status, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by species
     * GET /api/v1/pets/species/{species}
     */
    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable String species) {
        log.info("Getting pets by species: {}", species);
        PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
        List<Pet> pets = petService.findBySpecies(petSpecies);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by species with pagination
     * GET /api/v1/pets/species/{species}/page
     */
    @GetMapping("/species/{species}/page")
    public ResponseEntity<Page<Pet>> getPetsBySpeciesWithPagination(@PathVariable String species, Pageable pageable) {
        log.info("Getting pets by species with pagination: {}", species);
        PetSpecies petSpecies = PetSpecies.valueOf(species.toUpperCase());
        Page<Pet> pets = petService.findBySpecies(petSpecies, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by breed
     * GET /api/v1/pets/breed/{breed}
     */
    @GetMapping("/breed/{breed}")
    public ResponseEntity<List<Pet>> getPetsByBreed(@PathVariable String breed) {
        log.info("Getting pets by breed: {}", breed);
        List<Pet> pets = petService.findByBreed(breed);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by breed with pagination
     * GET /api/v1/pets/breed/{breed}/page
     */
    @GetMapping("/breed/{breed}/page")
    public ResponseEntity<Page<Pet>> getPetsByBreedWithPagination(@PathVariable String breed, Pageable pageable) {
        log.info("Getting pets by breed with pagination: {}", breed);
        Page<Pet> pets = petService.findByBreed(breed, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by age range
     * GET /api/v1/pets/age?min={minAge}&max={maxAge}
     */
    @GetMapping("/age")
    public ResponseEntity<List<Pet>> getPetsByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        log.info("Getting pets by age range: {} to {}", minAge, maxAge);
        List<Pet> pets = petService.findByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by age range with pagination
     * GET /api/v1/pets/age/page?min={minAge}&max={maxAge}
     */
    @GetMapping("/age/page")
    public ResponseEntity<Page<Pet>> getPetsByAgeRangeWithPagination(@RequestParam Integer minAge, @RequestParam Integer maxAge, Pageable pageable) {
        log.info("Getting pets by age range with pagination: {} to {}", minAge, maxAge);
        Page<Pet> pets = petService.findByAgeBetween(minAge, maxAge, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get available pets for adoption
     * GET /api/v1/pets/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<Pet>> getAvailablePets() {
        log.info("Getting available pets for adoption");
        List<Pet> pets = petService.findAvailableForAdoption();
        return ResponseEntity.ok(pets);
    }

    /**
     * Get available pets for adoption with pagination
     * GET /api/v1/pets/available/page
     */
    @GetMapping("/available/page")
    public ResponseEntity<Page<Pet>> getAvailablePetsWithPagination(Pageable pageable) {
        log.info("Getting available pets for adoption with pagination");
        Page<Pet> pets = petService.findAvailableForAdoption(pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Search pets by name
     * GET /api/v1/pets/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Pet>> searchPetsByName(@RequestParam String name) {
        log.info("Searching pets by name: {}", name);
        List<Pet> pets = petService.findByNameContaining(name);
        return ResponseEntity.ok(pets);
    }

    /**
     * Search pets by name with pagination
     * GET /api/v1/pets/search/page?name={name}
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<Pet>> searchPetsByNameWithPagination(@RequestParam String name, Pageable pageable) {
        log.info("Searching pets by name with pagination: {}", name);
        Page<Pet> pets = petService.findByNameContaining(name, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by foundation and status
     * GET /api/v1/pets/foundation/{foundationId}/status/{status}
     */
    @GetMapping("/foundation/{foundationId}/status/{status}")
    public ResponseEntity<List<Pet>> getPetsByFoundationAndStatus(@PathVariable UUID foundationId, @PathVariable PetStatus status) {
        log.info("Getting pets by foundation ID {} and status: {}", foundationId, status);
        List<Pet> pets = petService.findByFoundationIdAndStatus(foundationId, status);
        return ResponseEntity.ok(pets);
    }

    /**
     * Get pets by foundation and status with pagination
     * GET /api/v1/pets/foundation/{foundationId}/status/{status}/page
     */
    @GetMapping("/foundation/{foundationId}/status/{status}/page")
    public ResponseEntity<Page<Pet>> getPetsByFoundationAndStatusWithPagination(@PathVariable UUID foundationId, @PathVariable PetStatus status, Pageable pageable) {
        log.info("Getting pets by foundation ID {} and status with pagination: {}", foundationId, status);
        Page<Pet> pets = petService.findByFoundationIdAndStatus(foundationId, status, pageable);
        return ResponseEntity.ok(pets);
    }

    /**
     * Update pet
     * PUT /api/v1/pets/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update pet", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @Valid @RequestBody Pet pet) {
        log.info("Updating pet with ID: {}", id);
        try {
            Pet updatedPet = petService.updatePet(id, pet);
            return ResponseEntity.ok(updatedPet);
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
    public ResponseEntity<Pet> updatePetStatus(@PathVariable UUID id, @RequestBody PetStatus status) {
        log.info("Updating status for pet ID {} to: {}", id, status);
        try {
            Pet updatedPet = petService.updateStatus(id, status);
            return ResponseEntity.ok(updatedPet);
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
    public ResponseEntity<Pet> adoptPet(@PathVariable UUID id) {
        log.info("Marking pet as adopted: {}", id);
        try {
            Pet adoptedPet = petService.markAsAdopted(id);
            return ResponseEntity.ok(adoptedPet);
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
    public ResponseEntity<Pet> markPetAsAvailable(@PathVariable UUID id) {
        log.info("Marking pet as available: {}", id);
        try {
            Pet availablePet = petService.markAsAvailable(id);
            return ResponseEntity.ok(availablePet);
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

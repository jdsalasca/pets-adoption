package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetSpecies;
import com.petfriendly.backend.entity.PetStatus;
import com.petfriendly.backend.entity.PetSize;
import com.petfriendly.backend.entity.PetGender;
import com.petfriendly.backend.entity.Foundation;
import com.petfriendly.backend.repository.PetRepository;
import com.petfriendly.backend.repository.FoundationRepository;
import com.petfriendly.backend.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of PetService interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {

    // Manual logger since Lombok @Slf4j is not working
private final PetRepository petRepository;
    private final FoundationRepository foundationRepository;

    @Override
    public Pet createPet(Pet pet) {
        log.info("Creating new pet with name: {}", pet.getName());
        
        // Validate foundation exists
        if (pet.getFoundation() != null && pet.getFoundation().getId() != null) {
            foundationRepository.findById(pet.getFoundation().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + pet.getFoundation().getId()));
        }

        // Set default values
        if (pet.getStatus() == null) {
            pet.setStatus(PetStatus.AVAILABLE);
        }

        Pet savedPet = petRepository.save(pet);
        log.info("Pet created successfully with ID: {}", savedPet.getId());
        return savedPet;
    }

    @Override
    public Pet updatePet(UUID id, Pet pet) {
        log.info("Updating pet with ID: {}", id);
        
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + id));

        // Update fields
        if (pet.getName() != null) {
            existingPet.setName(pet.getName());
        }
        if (pet.getSpecies() != null) {
            existingPet.setSpecies(pet.getSpecies());
        }
        if (pet.getBreed() != null) {
            existingPet.setBreed(pet.getBreed());
        }
        if (pet.getAge() != null) {
            existingPet.setAge(pet.getAge());
        }
        if (pet.getGender() != null) {
            existingPet.setGender(pet.getGender());
        }
        if (pet.getSize() != null) {
            existingPet.setSize(pet.getSize());
        }
        if (pet.getDescription() != null) {
            existingPet.setDescription(pet.getDescription());
        }
        if (pet.getStatus() != null) {
            existingPet.setStatus(pet.getStatus());
        }

        existingPet.setUpdatedAt(LocalDateTime.now());

        Pet updatedPet = petRepository.save(existingPet);
        log.info("Pet updated successfully with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findById(UUID id) {
        log.debug("Finding pet by ID: {}", id);
        return petRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findAll(Pageable pageable) {
        log.debug("Finding all pets with pagination");
        return petRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByStatus(PetStatus status) {
        log.debug("Finding pets by status: {}", status);
        return petRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByStatus(PetStatus status, Pageable pageable) {
        log.debug("Finding pets by status with pagination: {}", status);
        return petRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findBySpecies(PetSpecies species) {
        log.debug("Finding pets by species: {}", species);
        return petRepository.findBySpecies(species);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findBySpecies(PetSpecies species, Pageable pageable) {
        log.debug("Finding pets by species with pagination: {}", species);
        return petRepository.findBySpecies(species, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByBreed(String breed) {
        log.debug("Finding pets by breed: {}", breed);
        return petRepository.findByBreed(breed);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByBreed(String breed, Pageable pageable) {
        log.debug("Finding pets by breed with pagination: {}", breed);
        return petRepository.findByBreed(breed, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByFoundation(Foundation foundation) {
        log.debug("Finding pets by foundation: {}", foundation.getName());
        return petRepository.findByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByFoundationId(UUID foundationId) {
        log.debug("Finding pets by foundation ID: {}", foundationId);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.findByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByFoundationId(UUID foundationId, Pageable pageable) {
        log.debug("Finding pets by foundation ID with pagination: {}", foundationId);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.findByFoundation(foundation, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByFoundationIdAndStatus(UUID foundationId, PetStatus status) {
        log.debug("Finding pets by foundation ID {} and status: {}", foundationId, status);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.findAllByFoundationAndStatus(foundation, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByFoundationIdAndStatus(UUID foundationId, PetStatus status, Pageable pageable) {
        log.debug("Finding pets by foundation ID {} and status with pagination: {}", foundationId, status);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.findByFoundationAndStatus(foundation, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> searchByName(String name) {
        log.debug("Searching pets by name: {}", name);
        return petRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByNameContaining(String name) {
        log.debug("Finding pets by name containing: {}", name);
        return petRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByNameContaining(String name, Pageable pageable) {
        log.debug("Finding pets by name containing with pagination: {}", name);
        return petRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findAvailablePetsByCity(String city) {
        log.debug("Finding available pets by city: {}", city);
        return petRepository.findAvailablePetsByCity(city);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findAvailableForAdoption() {
        log.debug("Finding available pets for adoption");
        return petRepository.findByStatus(PetStatus.AVAILABLE);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findAvailableForAdoption(Pageable pageable) {
        log.debug("Finding available pets for adoption with pagination");
        return petRepository.findByStatus(PetStatus.AVAILABLE, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findAvailablePetsWithFilters(PetSpecies species, PetSize size, 
                                                PetGender gender, String city, Pageable pageable) {
        log.debug("Finding available pets with filters - species: {}, size: {}, gender: {}, city: {}", 
                 species, size, gender, city);
        return petRepository.findAvailablePetsWithFilters(species, size, gender, city, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        log.debug("Checking if pet exists by ID: {}", id);
        return petRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total count of pets");
        return petRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(PetStatus status) {
        log.debug("Counting pets by status: {}", status);
        return petRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundation(Foundation foundation) {
        log.debug("Counting pets by foundation: {}", foundation.getName());
        return petRepository.countByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundationId(UUID foundationId) {
        log.debug("Counting pets by foundation ID: {}", foundationId);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.countByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundationIdAndStatus(UUID foundationId, PetStatus status) {
        log.debug("Counting pets by foundation ID {} and status: {}", foundationId, status);
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));
        return petRepository.countByFoundationAndStatus(foundation, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findPetsWithImages() {
        log.debug("Finding pets with images");
        return petRepository.findPetsWithImages();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findRecentlyAddedAvailablePets(Pageable pageable) {
        log.debug("Finding recently added available pets");
        return petRepository.findRecentlyAddedAvailablePets(pageable);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting pet with ID: {}", id);
        
        if (!petRepository.existsById(id)) {
            throw new IllegalArgumentException("Pet not found with ID: " + id);
        }

        petRepository.deleteById(id);
        log.info("Pet deleted successfully with ID: {}", id);
    }

    @Override
    public Pet updateStatus(UUID id, PetStatus status) {
        log.info("Updating status for pet with ID: {} to {}", id, status);
        
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + id));

        pet.setStatus(status);
        pet.setUpdatedAt(LocalDateTime.now());

        Pet updatedPet = petRepository.save(pet);
        log.info("Pet status updated successfully for ID: {}", updatedPet.getId());
        return updatedPet;
    }

    @Override
    public Pet markAsAdopted(UUID id) {
        log.info("Marking pet as adopted with ID: {}", id);
        return updateStatus(id, PetStatus.ADOPTED);
    }

    @Override
    public Pet markAsAvailable(UUID id) {
        log.info("Marking pet as available with ID: {}", id);
        return updateStatus(id, PetStatus.AVAILABLE);
    }

    @Override
    public Pet updateProfile(UUID id, String name, String breed, Integer age, String description) {
        log.info("Updating profile for pet with ID: {}", id);
        
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + id));

        if (name != null && !name.trim().isEmpty()) {
            pet.setName(name.trim());
        }
        if (breed != null && !breed.trim().isEmpty()) {
            pet.setBreed(breed.trim());
        }
        if (age != null && age > 0) {
            pet.setAge(age);
        }
        if (description != null && !description.trim().isEmpty()) {
            pet.setDescription(description.trim());
        }

        pet.setUpdatedAt(LocalDateTime.now());

        Pet updatedPet = petRepository.save(pet);
        log.info("Profile updated successfully for pet with ID: {}", updatedPet.getId());
        return updatedPet;
    }

    @Override
    @Transactional(readOnly = true)
    public PetStatistics getPetStatistics(UUID foundationId) {
        log.debug("Getting pet statistics for foundation with ID: {}", foundationId);
        
        Foundation foundation = foundationRepository.findById(foundationId)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + foundationId));

        long totalPets = petRepository.countByFoundation(foundation);
        long availablePets = petRepository.countByFoundationAndStatus(foundation, PetStatus.AVAILABLE);
        long adoptedPets = petRepository.countByFoundationAndStatus(foundation, PetStatus.ADOPTED);
        long pendingPets = petRepository.countByFoundationAndStatus(foundation, PetStatus.PENDING);
        long unavailablePets = petRepository.countByFoundationAndStatus(foundation, PetStatus.UNAVAILABLE);

        return new PetStatistics(totalPets, availablePets, adoptedPets, pendingPets, unavailablePets);
    }

    @Override
    @Transactional(readOnly = true)
    public PetStatistics getStatistics() {
        log.debug("Getting pet statistics");
        
        long totalPets = petRepository.count();
        long availablePets = petRepository.countByStatus(PetStatus.AVAILABLE);
        long adoptedPets = petRepository.countByStatus(PetStatus.ADOPTED);
        long pendingPets = petRepository.countByStatus(PetStatus.PENDING);
        long unavailablePets = petRepository.countByStatus(PetStatus.UNAVAILABLE);
        
        return new PetStatistics(totalPets, availablePets, adoptedPets, pendingPets, unavailablePets);
    }

    @Override
    @Transactional(readOnly = true)
    public PetStatistics getStatisticsByFoundation(UUID foundationId) {
        return getPetStatistics(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findByAgeBetween(Integer minAge, Integer maxAge) {
        log.debug("Finding pets by age range: {} to {}", minAge, maxAge);
        return petRepository.findByAgeBetween(minAge, maxAge);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> findByAgeBetween(Integer minAge, Integer maxAge, Pageable pageable) {
        log.debug("Finding pets by age range: {} to {} with pagination", minAge, maxAge);
        return petRepository.findByAgeBetween(minAge, maxAge, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countBySpecies(String species) {
        log.debug("Counting pets by species: {}", species);
        return petRepository.countBySpecies(species);
    }
}
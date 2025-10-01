package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetSpecies;
import com.petfriendly.backend.entity.PetStatus;
import com.petfriendly.backend.entity.PetSize;
import com.petfriendly.backend.entity.PetGender;
import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Pet entity operations
 */
public interface PetService {

    /**
     * Create a new pet
     * @param pet the pet to create
     * @return the created pet
     */
    Pet createPet(Pet pet);

    /**
     * Update an existing pet
     * @param id the pet ID
     * @param pet the updated pet data
     * @return the updated pet
     */
    Pet updatePet(UUID id, Pet pet);

    /**
     * Find pet by ID
     * @param id the pet ID
     * @return optional containing the pet if found
     */
    Optional<Pet> findById(UUID id);

    /**
     * Find all pets with pagination
     * @param pageable pagination information
     * @return page of pets
     */
    Page<Pet> findAll(Pageable pageable);

    /**
     * Find pets by status
     * @param status the pet status
     * @return list of pets with the specified status
     */
    List<Pet> findByStatus(PetStatus status);

    /**
     * Find pets by status with pagination
     * @param status the pet status
     * @param pageable pagination information
     * @return page of pets with the specified status
     */
    Page<Pet> findByStatus(PetStatus status, Pageable pageable);

    /**
     * Find pets by species
     * @param species the pet species
     * @return list of pets of the specified species
     */
    List<Pet> findBySpecies(PetSpecies species);

    /**
     * Find pets by species with pagination
     * @param species the pet species
     * @param pageable pagination information
     * @return page of pets of the specified species
     */
    Page<Pet> findBySpecies(PetSpecies species, Pageable pageable);

    /**
     * Find pets by foundation
     * @param foundation the foundation
     * @return list of pets belonging to the foundation
     */
    List<Pet> findByFoundation(Foundation foundation);

    /**
     * Find pets by foundation ID
     * @param foundationId the foundation ID
     * @return list of pets belonging to the foundation
     */
    List<Pet> findByFoundationId(UUID foundationId);

    /**
     * Find pets by foundation ID with pagination
     * @param foundationId the foundation ID
     * @param pageable pagination information
     * @return page of pets belonging to the foundation
     */
    Page<Pet> findByFoundationId(UUID foundationId, Pageable pageable);

    /**
     * Search pets by name
     * @param name the name to search for
     * @return list of pets whose name contains the search term
     */
    List<Pet> searchByName(String name);

    /**
     * Find pets by breed
     * @param breed the breed to search for
     * @return list of pets of the specified breed
     */
    List<Pet> findByBreed(String breed);

    /**
     * Find pets by breed with pagination
     * @param breed the breed to search for
     * @param pageable pagination information
     * @return page of pets of the specified breed
     */
    Page<Pet> findByBreed(String breed, Pageable pageable);

    /**
     * Find available pets for adoption
     * @return list of available pets
     */
    List<Pet> findAvailableForAdoption();

    /**
     * Find available pets for adoption with pagination
     * @param pageable pagination information
     * @return page of available pets
     */
    Page<Pet> findAvailableForAdoption(Pageable pageable);

    /**
     * Find pets by name containing search term
     * @param name the name search term
     * @return list of pets whose name contains the search term
     */
    List<Pet> findByNameContaining(String name);

    /**
     * Find pets by name containing search term with pagination
     * @param name the name search term
     * @param pageable pagination information
     * @return page of pets whose name contains the search term
     */
    Page<Pet> findByNameContaining(String name, Pageable pageable);

    /**
     * Find pets by foundation ID and status
     * @param foundationId the foundation ID
     * @param status the pet status
     * @return list of pets belonging to the foundation with the specified status
     */
    List<Pet> findByFoundationIdAndStatus(UUID foundationId, PetStatus status);

    /**
     * Find pets by foundation ID and status with pagination
     * @param foundationId the foundation ID
     * @param status the pet status
     * @param pageable pagination information
     * @return page of pets belonging to the foundation with the specified status
     */
    Page<Pet> findByFoundationIdAndStatus(UUID foundationId, PetStatus status, Pageable pageable);

    /**
     * Check if pet exists by ID
     * @param id the pet ID
     * @return true if pet exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Count total number of pets
     * @return total number of pets
     */
    long count();

    /**
     * Count pets by foundation ID
     * @param foundationId the foundation ID
     * @return number of pets belonging to the foundation
     */
    long countByFoundationId(UUID foundationId);

    /**
     * Count pets by foundation ID and status
     * @param foundationId the foundation ID
     * @param status the pet status
     * @return number of pets belonging to the foundation with the specified status
     */
    long countByFoundationIdAndStatus(UUID foundationId, PetStatus status);

    /**
     * Count pets by species
     * @param species the pet species as string
     * @return number of pets of the specified species
     */
    long countBySpecies(String species);

    /**
     * Get general statistics
     * @return general pet statistics
     */
    PetStatistics getStatistics();

    /**
     * Find pets by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of pets within the age range
     */
    List<Pet> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Find pets by age range with pagination
     * @param minAge minimum age
     * @param maxAge maximum age
     * @param pageable pagination information
     * @return page of pets within the age range
     */
    Page<Pet> findByAgeBetween(Integer minAge, Integer maxAge, Pageable pageable);

    /**
     * Get pet statistics for a specific foundation
     * @param foundationId the foundation ID
     * @return pet statistics for the foundation
     */
    PetStatistics getStatisticsByFoundation(UUID foundationId);

    /**
     * Find available pets by city
     * @param city the city name
     * @return list of available pets in the specified city
     */
    List<Pet> findAvailablePetsByCity(String city);

    /**
     * Find available pets with filters
     * @param species the pet species (optional)
     * @param size the pet size (optional)
     * @param gender the pet gender (optional)
     * @param city the city (optional)
     * @param pageable pagination information
     * @return page of filtered available pets
     */
    Page<Pet> findAvailablePetsWithFilters(PetSpecies species, PetSize size, 
                                         PetGender gender, String city, Pageable pageable);

    /**
     * Count pets by status
     * @param status the pet status
     * @return number of pets with the specified status
     */
    long countByStatus(PetStatus status);

    /**
     * Count pets by foundation
     * @param foundation the foundation
     * @return number of pets belonging to the foundation
     */
    long countByFoundation(Foundation foundation);

    /**
     * Find pets with images
     * @return list of pets that have at least one image
     */
    List<Pet> findPetsWithImages();

    /**
     * Find recently added available pets
     * @param pageable pagination information
     * @return page of recently added available pets
     */
    Page<Pet> findRecentlyAddedAvailablePets(Pageable pageable);

    /**
     * Delete pet by ID
     * @param id the pet ID
     */
    void deleteById(UUID id);

    /**
     * Update pet status
     * @param id the pet ID
     * @param status the new status
     * @return the updated pet
     */
    Pet updateStatus(UUID id, PetStatus status);

    /**
     * Mark pet as adopted
     * @param id the pet ID
     * @return the updated pet
     */
    Pet markAsAdopted(UUID id);

    /**
     * Mark pet as available
     * @param id the pet ID
     * @return the updated pet
     */
    Pet markAsAvailable(UUID id);

    /**
     * Update pet profile
     * @param id the pet ID
     * @param name the new name
     * @param breed the new breed
     * @param age the new age
     * @param description the new description
     * @return the updated pet
     */
    Pet updateProfile(UUID id, String name, String breed, Integer age, String description);

    /**
     * Get pet statistics for a foundation
     * @param foundationId the foundation ID
     * @return pet statistics for the foundation
     */
    PetStatistics getPetStatistics(UUID foundationId);

    /**
     * Inner class for pet statistics
     */
    class PetStatistics {
        private final long totalPets;
        private final long availablePets;
        private final long adoptedPets;
        private final long pendingPets;
        private final long unavailablePets;

        public PetStatistics(long totalPets, long availablePets, long adoptedPets, 
                           long pendingPets, long unavailablePets) {
            this.totalPets = totalPets;
            this.availablePets = availablePets;
            this.adoptedPets = adoptedPets;
            this.pendingPets = pendingPets;
            this.unavailablePets = unavailablePets;
        }

        public long getTotalPets() { return totalPets; }
        public long getAvailablePets() { return availablePets; }
        public long getAdoptedPets() { return adoptedPets; }
        public long getPendingPets() { return pendingPets; }
        public long getUnavailablePets() { return unavailablePets; }
    }
}
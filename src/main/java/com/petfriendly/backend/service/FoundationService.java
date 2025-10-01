package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for Foundation entity operations
 */
public interface FoundationService {

    /**
     * Create a new foundation
     * @param foundation the foundation to create
     * @return the created foundation
     */
    Foundation createFoundation(Foundation foundation);

    /**
     * Update an existing foundation
     * @param id the foundation ID
     * @param foundation the updated foundation data
     * @return the updated foundation
     */
    Foundation updateFoundation(UUID id, Foundation foundation);

    /**
     * Find foundation by ID
     * @param id the foundation ID
     * @return optional containing the foundation if found
     */
    Optional<Foundation> findById(UUID id);

    /**
     * Find foundation by contact email
     * @param contactEmail the contact email
     * @return optional containing the foundation if found
     */
    Optional<Foundation> findByContactEmail(String contactEmail);

    /**
     * Find all foundations with pagination
     * @param pageable pagination information
     * @return page of foundations
     */
    Page<Foundation> findAll(Pageable pageable);

    /**
     * Find all foundations
     * @return list of all foundations
     */
    List<Foundation> findAll();

    /**
     * Find foundation by name
     * @param name the foundation name
     * @return optional containing the foundation if found
     */
    Optional<Foundation> findByName(String name);

    /**
     * Find foundation by email
     * @param email the foundation email
     * @return optional containing the foundation if found
     */
    Optional<Foundation> findByEmail(String email);

    /**
     * Find foundations by state
     * @param state the state name
     * @return list of foundations in the specified state
     */
    List<Foundation> findByState(String state);

    /**
     * Find foundations by state with pagination
     * @param state the state name
     * @param pageable pagination information
     * @return page of foundations in the specified state
     */
    Page<Foundation> findByState(String state, Pageable pageable);

    /**
     * Find active foundations
     * @return list of active foundations
     */
    List<Foundation> findActiveFoundations();

    /**
     * Find active foundations with pagination
     * @param pageable pagination information
     * @return page of active foundations
     */
    Page<Foundation> findActiveFoundations(Pageable pageable);

    /**
     * Find foundations by name containing
     * @param name the name to search for
     * @return list of foundations whose name contains the search term
     */
    List<Foundation> findByNameContaining(String name);

    /**
     * Find foundations by name containing with pagination
     * @param name the name to search for
     * @param pageable pagination information
     * @return page of foundations whose name contains the search term
     */
    Page<Foundation> findByNameContaining(String name, Pageable pageable);

    /**
     * Activate foundation
     * @param id the foundation ID
     * @return the activated foundation
     */
    Foundation activateFoundation(UUID id);

    /**
     * Deactivate foundation
     * @param id the foundation ID
     * @return the deactivated foundation
     */
    Foundation deactivateFoundation(UUID id);

    /**
     * Check if foundation exists by ID
     * @param id the foundation ID
     * @return true if foundation exists, false otherwise
     */
    boolean existsById(UUID id);

    /**
     * Check if foundation exists by name
     * @param name the foundation name
     * @return true if foundation exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Count all foundations
     * @return total number of foundations
     */
    long count();

    /**
     * Count active foundations
     * @return number of active foundations
     */
    long countActiveFoundations();

    /**
     * Count foundations by city
     * @param city the city name
     * @return number of foundations in the specified city
     */
    long countByCity(String city);

    /**
     * Count foundations by state
     * @param state the state name
     * @return number of foundations in the specified state
     */
    long countByState(String state);

    /**
     * Get general foundation statistics
     * @return foundation statistics
     */
    FoundationStatistics getStatistics();

    /**
     * Find foundations by city
     * @param city the city name
     * @return list of foundations in the specified city
     */
    List<Foundation> findByCity(String city);

    /**
     * Find foundations by city with pagination
     * @param city the city name
     * @param pageable pagination information
     * @return page of foundations in the specified city
     */
    Page<Foundation> findByCity(String city, Pageable pageable);

    /**
     * Find foundations by verification status
     * @param verified the verification status
     * @return list of foundations with the specified verification status
     */
    List<Foundation> findByVerified(Boolean verified);

    /**
     * Search foundations by name
     * @param name the name to search for
     * @return list of foundations whose name contains the search term
     */
    List<Foundation> searchByName(String name);

    /**
     * Find foundations with available pets
     * @return list of foundations that have available pets
     */
    List<Foundation> findFoundationsWithAvailablePets();

    /**
     * Check if contact email exists
     * @param contactEmail the contact email to check
     * @return true if contact email exists, false otherwise
     */
    boolean existsByContactEmail(String contactEmail);

    /**
     * Check if email exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Count foundations by verification status
     * @param verified the verification status
     * @return number of foundations with the specified verification status
     */
    long countByVerified(Boolean verified);

    /**
     * Delete foundation by ID
     * @param id the foundation ID
     */
    void deleteById(UUID id);

    /**
     * Verify foundation
     * @param id the foundation ID
     * @return the verified foundation
     */
    Foundation verifyFoundation(UUID id);

    /**
     * Unverify foundation
     * @param id the foundation ID
     * @return the unverified foundation
     */
    Foundation unverifyFoundation(UUID id);

    /**
     * Update foundation profile
     * @param id the foundation ID
     * @param name the new name
     * @param description the new description
     * @param address the new address
     * @param city the new city
     * @param phoneNumber the new phone number
     * @param website the new website
     * @return the updated foundation
     */
    Foundation updateProfile(UUID id, String name, String description, String address, 
                           String city, String phoneNumber, String website);

    /**
     * Get foundation statistics
     * @param id the foundation ID
     * @return foundation statistics including pet counts
     */
    FoundationStatistics getFoundationStatistics(UUID id);

    /**
     * Inner class for foundation statistics
     */
    class FoundationStatistics {
        private final long totalPets;
        private final long availablePets;
        private final long adoptedPets;
        private final long pendingAdoptions;

        public FoundationStatistics(long totalPets, long availablePets, long adoptedPets, long pendingAdoptions) {
            this.totalPets = totalPets;
            this.availablePets = availablePets;
            this.adoptedPets = adoptedPets;
            this.pendingAdoptions = pendingAdoptions;
        }

        public long getTotalPets() { return totalPets; }
        public long getAvailablePets() { return availablePets; }
        public long getAdoptedPets() { return adoptedPets; }
        public long getPendingAdoptions() { return pendingAdoptions; }
    }
}
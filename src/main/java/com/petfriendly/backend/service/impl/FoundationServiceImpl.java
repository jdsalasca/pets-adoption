package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.Foundation;
import com.petfriendly.backend.entity.PetStatus;
import com.petfriendly.backend.entity.AdoptionRequestStatus;
import com.petfriendly.backend.repository.FoundationRepository;
import com.petfriendly.backend.repository.PetRepository;
import com.petfriendly.backend.repository.AdoptionRequestRepository;
import com.petfriendly.backend.service.FoundationService;
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
 * Implementation of FoundationService interface
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoundationServiceImpl implements FoundationService {

    private final FoundationRepository foundationRepository;
    private final PetRepository petRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;

    @Override
    public Foundation createFoundation(Foundation foundation) {
        log.info("Creating new foundation with name: {}", foundation.getName());
        
        if (existsByContactEmail(foundation.getContactEmail())) {
            throw new IllegalArgumentException("Foundation with contact email " + foundation.getContactEmail() + " already exists");
        }

        // Set default values
        if (foundation.getVerified() == null) {
            foundation.setVerified(false);
        }

        Foundation savedFoundation = foundationRepository.save(foundation);
        log.info("Foundation created successfully with ID: {}", savedFoundation.getId());
        return savedFoundation;
    }

    @Override
    public Foundation updateFoundation(UUID id, Foundation foundation) {
        log.info("Updating foundation with ID: {}", id);
        
        Foundation existingFoundation = foundationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + id));

        // Update fields
        if (foundation.getName() != null) {
            existingFoundation.setName(foundation.getName());
        }
        if (foundation.getDescription() != null) {
            existingFoundation.setDescription(foundation.getDescription());
        }
        if (foundation.getAddress() != null) {
            existingFoundation.setAddress(foundation.getAddress());
        }
        if (foundation.getCity() != null) {
            existingFoundation.setCity(foundation.getCity());
        }
        if (foundation.getPhoneNumber() != null) {
            existingFoundation.setPhoneNumber(foundation.getPhoneNumber());
        }
        if (foundation.getWebsite() != null) {
            existingFoundation.setWebsite(foundation.getWebsite());
        }
        if (foundation.getVerified() != null) {
            existingFoundation.setVerified(foundation.getVerified());
        }

        existingFoundation.setUpdatedAt(LocalDateTime.now());

        Foundation updatedFoundation = foundationRepository.save(existingFoundation);
        log.info("Foundation updated successfully with ID: {}", updatedFoundation.getId());
        return updatedFoundation;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Foundation> findById(UUID id) {
        log.debug("Finding foundation by ID: {}", id);
        return foundationRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Foundation> findByContactEmail(String contactEmail) {
        log.debug("Finding foundation by contact email: {}", contactEmail);
        return foundationRepository.findByContactEmail(contactEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Foundation> findAll(Pageable pageable) {
        log.debug("Finding all foundations with pagination");
        return foundationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findByCity(String city) {
        log.debug("Finding foundations by city: {}", city);
        return foundationRepository.findByCity(city);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Foundation> findByCity(String city, Pageable pageable) {
        log.debug("Finding foundations by city with pagination: {}", city);
        return foundationRepository.findByCity(city, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findByVerified(Boolean verified) {
        log.debug("Finding foundations by verified status: {}", verified);
        return foundationRepository.findByVerified(verified);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> searchByName(String name) {
        log.debug("Searching foundations by name: {}", name);
        return foundationRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findFoundationsWithAvailablePets() {
        log.debug("Finding foundations with available pets");
        return foundationRepository.findFoundationsWithAvailablePets();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByContactEmail(String contactEmail) {
        log.debug("Checking if contact email exists: {}", contactEmail);
        return foundationRepository.existsByContactEmail(contactEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        log.debug("Checking if email exists: {}", email);
        return foundationRepository.existsByContactEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByVerified(Boolean verified) {
        log.debug("Counting foundations by verified status: {}", verified);
        return foundationRepository.countByVerified(verified);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting foundation with ID: {}", id);
        
        if (!foundationRepository.existsById(id)) {
            throw new IllegalArgumentException("Foundation not found with ID: " + id);
        }

        foundationRepository.deleteById(id);
        log.info("Foundation deleted successfully with ID: {}", id);
    }

    @Override
    public Foundation verifyFoundation(UUID id) {
        log.info("Verifying foundation with ID: {}", id);
        
        Foundation foundation = foundationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + id));

        foundation.setVerified(true);
        foundation.setUpdatedAt(LocalDateTime.now());

        Foundation verifiedFoundation = foundationRepository.save(foundation);
        log.info("Foundation verified successfully with ID: {}", verifiedFoundation.getId());
        return verifiedFoundation;
    }

    @Override
    public Foundation unverifyFoundation(UUID id) {
        log.info("Unverifying foundation with ID: {}", id);
        
        Foundation foundation = foundationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + id));

        foundation.setVerified(false);
        foundation.setUpdatedAt(LocalDateTime.now());

        Foundation unverifiedFoundation = foundationRepository.save(foundation);
        log.info("Foundation unverified successfully with ID: {}", unverifiedFoundation.getId());
        return unverifiedFoundation;
    }

    @Override
    public Foundation updateProfile(UUID id, String name, String description, String address, 
                                  String city, String phoneNumber, String website) {
        log.info("Updating profile for foundation with ID: {}", id);
        
        Foundation foundation = foundationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + id));

        if (name != null && !name.trim().isEmpty()) {
            foundation.setName(name.trim());
        }
        if (description != null && !description.trim().isEmpty()) {
            foundation.setDescription(description.trim());
        }
        if (address != null && !address.trim().isEmpty()) {
            foundation.setAddress(address.trim());
        }
        if (city != null && !city.trim().isEmpty()) {
            foundation.setCity(city.trim());
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            foundation.setPhoneNumber(phoneNumber.trim());
        }
        if (website != null && !website.trim().isEmpty()) {
            foundation.setWebsite(website.trim());
        }

        foundation.setUpdatedAt(LocalDateTime.now());

        Foundation updatedFoundation = foundationRepository.save(foundation);
        log.info("Profile updated successfully for foundation with ID: {}", updatedFoundation.getId());
        return updatedFoundation;
    }

    @Override
    @Transactional(readOnly = true)
    public FoundationStatistics getFoundationStatistics(UUID id) {
        log.debug("Getting statistics for foundation with ID: {}", id);
        
        Foundation foundation = foundationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foundation not found with ID: " + id));

        long totalPets = petRepository.countByFoundation(foundation);
        long availablePets = petRepository.countByFoundationAndStatus(foundation, PetStatus.AVAILABLE);
        long adoptedPets = petRepository.countByFoundationAndStatus(foundation, PetStatus.ADOPTED);
        long pendingAdoptions = adoptionRequestRepository.countByPetFoundationAndStatus(foundation, AdoptionRequestStatus.PENDING);

        return new FoundationStatistics(totalPets, availablePets, adoptedPets, pendingAdoptions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findAll() {
        log.debug("Finding all foundations");
        return foundationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Foundation> findByName(String name) {
        log.debug("Finding foundation by name: {}", name);
        return foundationRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Foundation> findByEmail(String email) {
        log.debug("Finding foundation by email: {}", email);
        return foundationRepository.findByContactEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findByState(String state) {
        log.debug("Finding foundations by state: {}", state);
        return foundationRepository.findByState(state);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Foundation> findByState(String state, Pageable pageable) {
        log.debug("Finding foundations by state with pagination: {}", state);
        return foundationRepository.findByState(state, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findActiveFoundations() {
        log.debug("Finding active foundations");
        return foundationRepository.findByVerified(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Foundation> findActiveFoundations(Pageable pageable) {
        log.debug("Finding active foundations with pagination");
        return foundationRepository.findByVerified(true, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Foundation> findByNameContaining(String name) {
        log.debug("Finding foundations by name containing: {}", name);
        return foundationRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Foundation> findByNameContaining(String name, Pageable pageable) {
        log.debug("Finding foundations by name containing with pagination: {}", name);
        return foundationRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Foundation activateFoundation(UUID id) {
        log.info("Activating foundation with ID: {}", id);
        return verifyFoundation(id);
    }

    @Override
    public Foundation deactivateFoundation(UUID id) {
        log.info("Deactivating foundation with ID: {}", id);
        return unverifyFoundation(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        log.debug("Checking if foundation exists by ID: {}", id);
        return foundationRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Checking if foundation exists by name: {}", name);
        return foundationRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Counting all foundations");
        return foundationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveFoundations() {
        log.debug("Counting active foundations");
        return foundationRepository.countByVerified(true);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCity(String city) {
        log.debug("Counting foundations by city: {}", city);
        return foundationRepository.countByCity(city);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByState(String state) {
        log.debug("Counting foundations by state: {}", state);
        return foundationRepository.countByState(state);
    }

    @Override
    @Transactional(readOnly = true)
    public FoundationStatistics getStatistics() {
        log.debug("Getting general foundation statistics");
        
        long totalPets = petRepository.count();
        long availablePets = petRepository.countByStatus(PetStatus.AVAILABLE);
        long adoptedPets = petRepository.countByStatus(PetStatus.ADOPTED);
        long pendingAdoptions = adoptionRequestRepository.countByStatus(AdoptionRequestStatus.PENDING);
        return new FoundationStatistics(totalPets, availablePets, adoptedPets, pendingAdoptions);
    }
}
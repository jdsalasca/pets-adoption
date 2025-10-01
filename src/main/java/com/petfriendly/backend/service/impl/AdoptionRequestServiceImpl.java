package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.AdoptionRequest;
import com.petfriendly.backend.entity.AdoptionRequestStatus;
import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.repository.AdoptionRequestRepository;
import com.petfriendly.backend.service.AdoptionRequestService;
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
 * Implementation of {@link AdoptionRequestService} backed by Spring Data repositories.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdoptionRequestServiceImpl implements AdoptionRequestService {

    private final AdoptionRequestRepository adoptionRequestRepository;

    @Override
    public AdoptionRequest createAdoptionRequest(AdoptionRequest adoptionRequest) {
        log.debug("Creating adoption request for user {} and pet {}", adoptionRequest.getUser().getId(), adoptionRequest.getPet().getId());

        if (adoptionRequestRepository.existsByUserAndPet(adoptionRequest.getUser(), adoptionRequest.getPet())) {
            throw new IllegalStateException("User has already submitted a request for this pet");
        }

        adoptionRequest.setStatus(AdoptionRequestStatus.PENDING);
        adoptionRequest.setCreatedAt(LocalDateTime.now());
        adoptionRequest.setUpdatedAt(LocalDateTime.now());

        AdoptionRequest saved = adoptionRequestRepository.save(adoptionRequest);
        log.info("Adoption request {} created", saved.getId());
        return saved;
    }

    @Override
    public AdoptionRequest updateAdoptionRequest(UUID id, AdoptionRequest adoptionRequest) {
        log.debug("Updating adoption request {}", id);

        AdoptionRequest existing = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adoption request not found: " + id));

        existing.setMessage(adoptionRequest.getMessage());
        existing.setExperience(adoptionRequest.getExperience());
        existing.setLivingSituation(adoptionRequest.getLivingSituation());
        existing.setUpdatedAt(LocalDateTime.now());

        return adoptionRequestRepository.save(existing);
    }

    @Override
    public AdoptionRequest updateStatus(UUID id, AdoptionRequestStatus status, String reviewNotes) {
        log.debug("Updating status of adoption request {} to {}", id, status);

        AdoptionRequest adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adoption request not found: " + id));

        adoptionRequest.setStatus(status);
        adoptionRequest.setReviewNotes(reviewNotes);
        adoptionRequest.setReviewedAt(LocalDateTime.now());
        adoptionRequest.setUpdatedAt(LocalDateTime.now());

        AdoptionRequest updated = adoptionRequestRepository.save(adoptionRequest);
        log.info("Adoption request {} status updated to {}", id, status);
        return updated;
    }

    @Override
    public AdoptionRequest updateStatus(UUID id, AdoptionRequestStatus status) {
        return updateStatus(id, status, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdoptionRequest> findById(UUID id) {
        return adoptionRequestRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findAll() {
        return adoptionRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findAll(Pageable pageable) {
        return adoptionRequestRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByUser(User user) {
        return adoptionRequestRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByUser(User user, Pageable pageable) {
        return adoptionRequestRepository.findByUser(user, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByUserId(UUID userId) {
        return adoptionRequestRepository.findByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByUserId(UUID userId, Pageable pageable) {
        return adoptionRequestRepository.findByUser_Id(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status) {
        return adoptionRequestRepository.findByUser_IdAndStatus(userId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status, Pageable pageable) {
        return adoptionRequestRepository.findByUser_IdAndStatus(userId, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByPet(Pet pet) {
        return adoptionRequestRepository.findByPet(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByPet(Pet pet, Pageable pageable) {
        return adoptionRequestRepository.findByPet(pet, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByPetId(UUID petId) {
        return adoptionRequestRepository.findByPet_Id(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByPetId(UUID petId, Pageable pageable) {
        return adoptionRequestRepository.findByPet_Id(petId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status) {
        return adoptionRequestRepository.findByPet_IdAndStatus(petId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status, Pageable pageable) {
        return adoptionRequestRepository.findByPet_IdAndStatus(petId, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdoptionRequest> findByStatus(AdoptionRequestStatus status) {
        return adoptionRequestRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByStatus(AdoptionRequestStatus status, Pageable pageable) {
        return adoptionRequestRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdoptionRequest> findByUserAndPet(User user, Pet pet) {
        return adoptionRequestRepository.findByUserAndPet(user, pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findPendingRequestsByFoundation(UUID foundationId, Pageable pageable) {
        return adoptionRequestRepository.findPendingRequestsByFoundation(foundationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdoptionRequest> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return adoptionRequestRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return adoptionRequestRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(AdoptionRequestStatus status) {
        return adoptionRequestRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUser(User user) {
        return adoptionRequestRepository.countByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPet(Pet pet) {
        return adoptionRequestRepository.countByPet(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserId(UUID userId) {
        return adoptionRequestRepository.countByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPetId(UUID petId) {
        return adoptionRequestRepository.countByPet_Id(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingRequestsByFoundation(UUID foundationId) {
        return adoptionRequestRepository.countPendingRequestsByFoundation(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRequestedAdoption(User user, Pet pet) {
        return adoptionRequestRepository.existsByUserAndPet(user, pet);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRequestedAdoption(UUID userId, UUID petId) {
        return adoptionRequestRepository.existsByUser_IdAndPet_Id(userId, petId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingRequests(User user) {
        return adoptionRequestRepository.existsByUserAndStatus(user, AdoptionRequestStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingRequests(UUID userId) {
        return adoptionRequestRepository.countByUser_IdAndStatus(userId, AdoptionRequestStatus.PENDING) > 0;
    }

    @Override
    public AdoptionRequest approveRequest(UUID id, String reviewNotes) {
        return updateStatus(id, AdoptionRequestStatus.APPROVED, reviewNotes);
    }

    @Override
    public AdoptionRequest approveRequest(UUID id) {
        return approveRequest(id, null);
    }

    @Override
    public AdoptionRequest rejectRequest(UUID id, String reviewNotes) {
        return updateStatus(id, AdoptionRequestStatus.REJECTED, reviewNotes);
    }

    @Override
    public AdoptionRequest rejectRequest(UUID id) {
        return rejectRequest(id, null);
    }

    @Override
    public AdoptionRequest cancelRequest(UUID id) {
        return updateStatus(id, AdoptionRequestStatus.CANCELLED, "Cancelled by user");
    }

    @Override
    public void deleteById(UUID id) {
        if (!adoptionRequestRepository.existsById(id)) {
            throw new IllegalArgumentException("Adoption request not found: " + id);
        }
        adoptionRequestRepository.deleteById(id);
        log.info("Adoption request {} deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return adoptionRequestRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestStatistics getStatistics() {
        long total = adoptionRequestRepository.count();
        long pending = adoptionRequestRepository.countByStatus(AdoptionRequestStatus.PENDING);
        long approved = adoptionRequestRepository.countByStatus(AdoptionRequestStatus.APPROVED);
        long rejected = adoptionRequestRepository.countByStatus(AdoptionRequestStatus.REJECTED);
        long cancelled = adoptionRequestRepository.countByStatus(AdoptionRequestStatus.CANCELLED);
        return new AdoptionRequestStatistics(total, pending, approved, rejected, cancelled);
    }

    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestStatistics getStatisticsByFoundation(UUID foundationId) {
        long total = adoptionRequestRepository.countByFoundation(foundationId);
        long pending = adoptionRequestRepository.countPendingRequestsByFoundation(foundationId);
        long approved = adoptionRequestRepository.countByFoundationAndStatus(foundationId, AdoptionRequestStatus.APPROVED);
        long rejected = adoptionRequestRepository.countByFoundationAndStatus(foundationId, AdoptionRequestStatus.REJECTED);
        long cancelled = adoptionRequestRepository.countByFoundationAndStatus(foundationId, AdoptionRequestStatus.CANCELLED);
        return new AdoptionRequestStatistics(total, pending, approved, rejected, cancelled);
    }

    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestStatistics getStatisticsByUser(UUID userId) {
        long total = adoptionRequestRepository.countByUser_Id(userId);
        long pending = adoptionRequestRepository.countByUser_IdAndStatus(userId, AdoptionRequestStatus.PENDING);
        long approved = adoptionRequestRepository.countByUser_IdAndStatus(userId, AdoptionRequestStatus.APPROVED);
        long rejected = adoptionRequestRepository.countByUser_IdAndStatus(userId, AdoptionRequestStatus.REJECTED);
        long cancelled = adoptionRequestRepository.countByUser_IdAndStatus(userId, AdoptionRequestStatus.CANCELLED);
        return new AdoptionRequestStatistics(total, pending, approved, rejected, cancelled);
    }

    @Override
    @Transactional(readOnly = true)
    public AdoptionRequestStatistics getStatisticsByPet(UUID petId) {
        long total = adoptionRequestRepository.countByPet_Id(petId);
        long pending = adoptionRequestRepository.countByPet_IdAndStatus(petId, AdoptionRequestStatus.PENDING);
        long approved = adoptionRequestRepository.countByPet_IdAndStatus(petId, AdoptionRequestStatus.APPROVED);
        long rejected = adoptionRequestRepository.countByPet_IdAndStatus(petId, AdoptionRequestStatus.REJECTED);
        long cancelled = adoptionRequestRepository.countByPet_IdAndStatus(petId, AdoptionRequestStatus.CANCELLED);
        return new AdoptionRequestStatistics(total, pending, approved, rejected, cancelled);
    }
}
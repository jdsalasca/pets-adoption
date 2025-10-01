package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.AdoptionRequest;
import com.petfriendly.backend.entity.AdoptionRequestStatus;
import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdoptionRequestService {

    // Creation & updates
    AdoptionRequest createAdoptionRequest(AdoptionRequest adoptionRequest);

    AdoptionRequest updateAdoptionRequest(UUID id, AdoptionRequest adoptionRequest);

    AdoptionRequest updateStatus(UUID id, AdoptionRequestStatus status, String reviewNotes);

    AdoptionRequest updateStatus(UUID id, AdoptionRequestStatus status);

    // Retrieval helpers
    Optional<AdoptionRequest> findById(UUID id);

    List<AdoptionRequest> findAll();

    Page<AdoptionRequest> findAll(Pageable pageable);

    List<AdoptionRequest> findByUser(User user);

    Page<AdoptionRequest> findByUser(User user, Pageable pageable);

    List<AdoptionRequest> findByUserId(UUID userId);

    Page<AdoptionRequest> findByUserId(UUID userId, Pageable pageable);

    List<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status);

    Page<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status, Pageable pageable);

    List<AdoptionRequest> findByPet(Pet pet);

    Page<AdoptionRequest> findByPet(Pet pet, Pageable pageable);

    List<AdoptionRequest> findByPetId(UUID petId);

    Page<AdoptionRequest> findByPetId(UUID petId, Pageable pageable);

    List<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status);

    Page<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status, Pageable pageable);

    List<AdoptionRequest> findByStatus(AdoptionRequestStatus status);

    Page<AdoptionRequest> findByStatus(AdoptionRequestStatus status, Pageable pageable);

    Optional<AdoptionRequest> findByUserAndPet(User user, Pet pet);

    Page<AdoptionRequest> findPendingRequestsByFoundation(UUID foundationId, Pageable pageable);

    Page<AdoptionRequest> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Counters
    long count();

    long countByStatus(AdoptionRequestStatus status);

    long countByUser(User user);

    long countByPet(Pet pet);

    long countByUserId(UUID userId);

    long countByPetId(UUID petId);

    long countPendingRequestsByFoundation(UUID foundationId);

    // Existence checks
    boolean hasUserRequestedAdoption(User user, Pet pet);

    boolean hasUserRequestedAdoption(UUID userId, UUID petId);

    boolean hasPendingRequests(User user);

    boolean hasPendingRequests(UUID userId);

    // Status transitions
    AdoptionRequest approveRequest(UUID id, String reviewNotes);

    AdoptionRequest approveRequest(UUID id);

    AdoptionRequest rejectRequest(UUID id, String reviewNotes);

    AdoptionRequest rejectRequest(UUID id);

    AdoptionRequest cancelRequest(UUID id);

    // Lifecycle
    void deleteById(UUID id);

    boolean existsById(UUID id);

    // Statistics
    AdoptionRequestStatistics getStatistics();

    AdoptionRequestStatistics getStatisticsByFoundation(UUID foundationId);

    AdoptionRequestStatistics getStatisticsByUser(UUID userId);

    AdoptionRequestStatistics getStatisticsByPet(UUID petId);

    class AdoptionRequestStatistics {
        private final long totalRequests;
        private final long pendingRequests;
        private final long approvedRequests;
        private final long rejectedRequests;
        private final long cancelledRequests;

        public AdoptionRequestStatistics(long totalRequests, long pendingRequests,
                                         long approvedRequests, long rejectedRequests,
                                         long cancelledRequests) {
            this.totalRequests = totalRequests;
            this.pendingRequests = pendingRequests;
            this.approvedRequests = approvedRequests;
            this.rejectedRequests = rejectedRequests;
            this.cancelledRequests = cancelledRequests;
        }

        public long getTotalRequests() {
            return totalRequests;
        }

        public long getPendingRequests() {
            return pendingRequests;
        }

        public long getApprovedRequests() {
            return approvedRequests;
        }

        public long getRejectedRequests() {
            return rejectedRequests;
        }

        public long getCancelledRequests() {
            return cancelledRequests;
        }
    }
}

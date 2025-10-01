package com.petfriendly.backend.repository;

import com.petfriendly.backend.entity.AdoptionRequest;
import com.petfriendly.backend.entity.AdoptionRequestStatus;
import com.petfriendly.backend.entity.Foundation;
import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for AdoptionRequest entity operations.
 */
@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, UUID> {

    List<AdoptionRequest> findByUser(User user);

    Page<AdoptionRequest> findByUser(User user, Pageable pageable);

    List<AdoptionRequest> findByUserId(UUID userId);

    Page<AdoptionRequest> findByUserId(UUID userId, Pageable pageable);

    List<AdoptionRequest> findByPet(Pet pet);

    Page<AdoptionRequest> findByPet(Pet pet, Pageable pageable);

    List<AdoptionRequest> findByPetId(UUID petId);

    Page<AdoptionRequest> findByPetId(UUID petId, Pageable pageable);

    List<AdoptionRequest> findByStatus(AdoptionRequestStatus status);

    Page<AdoptionRequest> findByStatus(AdoptionRequestStatus status, Pageable pageable);

    List<AdoptionRequest> findByUserAndStatus(User user, AdoptionRequestStatus status);

    List<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status);

    Page<AdoptionRequest> findByUserIdAndStatus(UUID userId, AdoptionRequestStatus status, Pageable pageable);

    List<AdoptionRequest> findByPetAndStatus(Pet pet, AdoptionRequestStatus status);

    List<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status);

    Page<AdoptionRequest> findByPetIdAndStatus(UUID petId, AdoptionRequestStatus status, Pageable pageable);

    Optional<AdoptionRequest> findByUserAndPet(User user, Pet pet);

    @Query("SELECT ar FROM AdoptionRequest ar JOIN ar.pet p WHERE p.foundation.id = :foundationId")
    List<AdoptionRequest> findByPetFoundationId(@Param("foundationId") UUID foundationId);

    @Query("SELECT ar FROM AdoptionRequest ar JOIN ar.pet p WHERE p.foundation.id = :foundationId")
    Page<AdoptionRequest> findByPetFoundationId(@Param("foundationId") UUID foundationId, Pageable pageable);

    @Query("SELECT ar FROM AdoptionRequest ar JOIN ar.pet p WHERE p.foundation.id = :foundationId AND ar.status = :status")
    List<AdoptionRequest> findByPetFoundationIdAndStatus(@Param("foundationId") UUID foundationId,
                                                         @Param("status") AdoptionRequestStatus status);

    @Query("SELECT COUNT(ar) FROM AdoptionRequest ar WHERE ar.pet.foundation.id = :foundationId")
    long countByFoundation(@Param("foundationId") UUID foundationId);

    @Query("SELECT COUNT(ar) FROM AdoptionRequest ar WHERE ar.pet.foundation.id = :foundationId AND ar.status = 'PENDING'")
    long countPendingRequestsByFoundation(@Param("foundationId") UUID foundationId);

    @Query("SELECT COUNT(ar) FROM AdoptionRequest ar WHERE ar.pet.foundation.id = :foundationId AND ar.status = :status")
    long countByFoundationAndStatus(@Param("foundationId") UUID foundationId,
                                    @Param("status") AdoptionRequestStatus status);

    @Query("SELECT COUNT(ar) FROM AdoptionRequest ar WHERE ar.pet.foundation = :foundation AND ar.status = :status")
    long countByPetFoundationAndStatus(@Param("foundation") Foundation foundation,
                                       @Param("status") AdoptionRequestStatus status);

    @Query("SELECT ar FROM AdoptionRequest ar WHERE ar.pet.foundation.id = :foundationId AND ar.status = 'PENDING'")
    Page<AdoptionRequest> findPendingRequestsByFoundation(@Param("foundationId") UUID foundationId, Pageable pageable);

    Page<AdoptionRequest> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    long countByStatus(AdoptionRequestStatus status);

    long countByUser(User user);

    long countByPet(Pet pet);
// Count methods
    long countByUserId(UUID userId);

    long countByPetId(UUID petId);

    long countByUserIdAndStatus(UUID userId, AdoptionRequestStatus status);

    long countByPetIdAndStatus(UUID petId, AdoptionRequestStatus status);

    boolean existsByUserAndPet(User user, Pet pet);

    boolean existsByUserAndStatus(User user, AdoptionRequestStatus status);

    boolean existsByUser_IdAndPet_Id(UUID userId, UUID petId);

    @Query("SELECT ar FROM AdoptionRequest ar ORDER BY ar.createdAt DESC")
    Page<AdoptionRequest> findRecentAdoptionRequests(Pageable pageable);
}
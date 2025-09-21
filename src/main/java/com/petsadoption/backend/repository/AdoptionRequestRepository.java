package com.petsadoption.backend.repository;

import com.petsadoption.backend.entity.AdoptionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, UUID> {

    Page<AdoptionRequest> findByUser_Id(UUID userId, Pageable pageable);

    Optional<AdoptionRequest> findByIdAndUser_Id(UUID id, UUID userId);

    Page<AdoptionRequest> findByPet_Foundation_Id(UUID foundationId, Pageable pageable);

    Page<AdoptionRequest> findByPet_Id(UUID petId, Pageable pageable);

    boolean existsByPet_IdAndUser_IdAndStatusIn(UUID petId, UUID userId, Collection<AdoptionRequest.Status> statuses);
}

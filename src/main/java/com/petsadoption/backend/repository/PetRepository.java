package com.petsadoption.backend.repository;

import com.petsadoption.backend.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    Page<Pet> findByFoundationId(UUID foundationId, Pageable pageable);

    Page<Pet> findByCityIgnoreCase(String city, Pageable pageable);

    @Query("""
            SELECT p
            FROM Pet p
            WHERE (:city IS NULL OR LOWER(p.city) = LOWER(:city))
              AND (:foundationId IS NULL OR p.foundation.id = :foundationId)
              AND (:species IS NULL OR p.species = :species)
              AND (:status IS NULL OR p.status = :status)
            ORDER BY p.createdAt DESC
            """)
    Page<Pet> findByFilters(
            @Param("city") String city,
            @Param("foundationId") UUID foundationId,
            @Param("species") Pet.Species species,
            @Param("status") Pet.Status status,
            Pageable pageable);

    boolean existsByIdAndFoundationId(UUID petId, UUID foundationId);

    Optional<Pet> findByIdAndFoundationId(UUID petId, UUID foundationId);
}

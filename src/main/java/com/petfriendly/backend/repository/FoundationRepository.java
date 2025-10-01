package com.petfriendly.backend.repository;

import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Foundation entity operations.
 */
@Repository
public interface FoundationRepository extends JpaRepository<Foundation, UUID> {

    Optional<Foundation> findByContactEmail(String contactEmail);

    Optional<Foundation> findByName(String name);

    Optional<Foundation> findByEmail(String contactEmail);

    List<Foundation> findByCity(String city);

    Page<Foundation> findByCity(String city, Pageable pageable);

    List<Foundation> findByState(String state);

    Page<Foundation> findByState(String state, Pageable pageable);

    List<Foundation> findByVerified(Boolean verified);

    Page<Foundation> findByVerified(Boolean verified, Pageable pageable);

    List<Foundation> findByCityAndVerified(String city, Boolean verified);

    @Query("SELECT f FROM Foundation f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Foundation> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT f FROM Foundation f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Foundation> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT f FROM Foundation f JOIN f.pets p WHERE p.status = 'AVAILABLE'")
    List<Foundation> findFoundationsWithAvailablePets();

    long countByVerified(Boolean verified);

    long countByCity(String city);

    long countByState(String state);

    boolean existsByContactEmail(String contactEmail);

    boolean existsByName(String name);
}
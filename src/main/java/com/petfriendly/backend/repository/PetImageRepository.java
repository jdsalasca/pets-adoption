package com.petfriendly.backend.repository;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetImage;
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
 * Repository interface for PetImage entity operations
 */
@Repository
public interface PetImageRepository extends JpaRepository<PetImage, UUID> {

    /**
     * Find all images for a specific pet
     * @param pet the pet to find images for
     * @return list of images belonging to the pet
     */
    List<PetImage> findByPet(Pet pet);

    /**
     * Find all images for a specific pet by pet ID
     * @param petId the pet ID to find images for
     * @return list of images belonging to the pet
     */
    List<PetImage> findByPetId(UUID petId);

    /**
     * Find all images for a specific pet by pet ID with pagination
     * @param petId the pet ID to find images for
     * @param pageable pagination information
     * @return page of images belonging to the pet
     */
    Page<PetImage> findByPetId(UUID petId, Pageable pageable);

    /**
     * Find the primary image for a specific pet
     * @param pet the pet to find the primary image for
     * @return Optional containing the primary image if found
     */
    Optional<PetImage> findByPetAndIsPrimaryTrue(Pet pet);

    /**
     * Find the primary image for a specific pet by pet ID
     * @param petId the pet ID to find the primary image for
     * @return Optional containing the primary image if found
     */
    Optional<PetImage> findByPetIdAndIsPrimaryTrue(UUID petId);

    /**
     * Find secondary (non-primary) images for a specific pet by pet ID
     * @param petId the pet ID to find images for
     * @return list of secondary images belonging to the pet
     */
    List<PetImage> findByPetIdAndIsPrimaryFalse(UUID petId);

    /**
     * Find secondary (non-primary) images for a specific pet by pet ID with pagination
     * @param petId the pet ID to find images for
     * @param pageable pagination information
     * @return page of secondary images belonging to the pet
     */
    Page<PetImage> findByPetIdAndIsPrimaryFalse(UUID petId, Pageable pageable);

    /**
     * Find images by URL containing pattern (case insensitive)
     * @param urlPattern the URL pattern to search for
     * @return list of images with URLs containing the pattern
     */
    List<PetImage> findByImageUrlContainingIgnoreCase(String urlPattern);

    /**
     * Find images by URL containing pattern with pagination (case insensitive)
     * @param urlPattern the URL pattern to search for
     * @param pageable pagination information
     * @return page of images with URLs containing the pattern
     */
    Page<PetImage> findByImageUrlContainingIgnoreCase(String urlPattern, Pageable pageable);

    /**
     * Check if a pet has a primary image by pet ID
     * @param petId the pet ID to check
     * @return true if the pet has a primary image, false otherwise
     */
    boolean existsByPetIdAndIsPrimaryTrue(UUID petId);

    /**
     * Count primary images for a specific pet by pet ID
     * @param petId the pet ID to count primary images for
     * @return number of primary images for the pet
     */
    long countByPetIdAndIsPrimaryTrue(UUID petId);

    /**
     * Find all primary images
     * @return list of all primary images
     */
    List<PetImage> findByIsPrimaryTrue();

    /**
     * Find images for a pet ordered by creation date
     * @param pet the pet to find images for
     * @return list of images ordered by creation date (newest first)
     */
    @Query("SELECT pi FROM PetImage pi WHERE pi.pet = :pet ORDER BY pi.createdAt DESC")
    List<PetImage> findByPetOrderByCreatedAtDesc(@Param("pet") Pet pet);

    /**
     * Count images for a specific pet
     * @param pet the pet to count images for
     * @return number of images for the pet
     */
    long countByPet(Pet pet);

    /**
     * Count images for a specific pet by pet ID
     * @param petId the pet ID to count images for
     * @return number of images for the pet
     */
    long countByPetId(UUID petId);

    /**
     * Check if a pet has a primary image
     * @param pet the pet to check
     * @return true if the pet has a primary image, false otherwise
     */
    boolean existsByPetAndIsPrimaryTrue(Pet pet);

    /**
     * Delete all images for a specific pet
     * @param pet the pet to delete images for
     */
    void deleteByPet(Pet pet);

    /**
     * Delete all images for a specific pet by pet ID
     * @param petId the pet ID to delete images for
     */
    void deleteByPetId(UUID petId);

    /**
     * Count primary images
     * @return number of primary images
     */
    long countByIsPrimaryTrue();

    /**
     * Count images without pet (orphaned images)
     * @return number of images without associated pet
     */
    long countByPetIsNull();

    /**
     * Count distinct pets that have images
     * @return number of distinct pets with images
     */
    @Query("SELECT COUNT(DISTINCT pi.pet) FROM PetImage pi WHERE pi.pet IS NOT NULL")
    long countDistinctPets();
}
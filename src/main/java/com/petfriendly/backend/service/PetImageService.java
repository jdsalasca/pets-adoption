package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for PetImage entity operations
 */
public interface PetImageService {

    /**
     * Create a new pet image
     * @param petImage the pet image to create
     * @return the created pet image
     */
    PetImage createPetImage(PetImage petImage);

    /**
     * Update an existing pet image
     * @param id the pet image ID
     * @param petImage the updated pet image data
     * @return the updated pet image
     */
    PetImage updatePetImage(UUID id, PetImage petImage);

    /**
     * Find pet image by ID
     * @param id the pet image ID
     * @return optional containing the pet image if found
     */
    Optional<PetImage> findById(UUID id);

    /**
     * Find all pet images
     * @return list of all pet images
     */
    List<PetImage> findAll();

    /**
     * Find all pet images with pagination
     * @param pageable pagination information
     * @return page of pet images
     */
    Page<PetImage> findAll(Pageable pageable);

    /**
     * Find pet images by pet
     * @param pet the pet
     * @return list of pet images for the pet
     */
    List<PetImage> findByPet(Pet pet);

    /**
     * Find pet images by pet ID
     * @param petId the pet ID
     * @return list of pet images for the pet
     */
    List<PetImage> findByPetId(UUID petId);

    /**
     * Find primary image for a pet
     * @param pet the pet
     * @return optional containing the primary image if found
     */
    Optional<PetImage> findPrimaryImageByPet(Pet pet);

    /**
     * Find primary image for a pet by pet ID
     * @param petId the pet ID
     * @return optional containing the primary image if found
     */
    Optional<PetImage> findPrimaryImageByPetId(UUID petId);

    /**
     * Find all primary images
     * @return list of all primary images
     */
    List<PetImage> findAllPrimaryImages();

    /**
     * Find pet images ordered by creation date
     * @param pet the pet
     * @return list of pet images ordered by creation date (newest first)
     */
    List<PetImage> findByPetOrderByCreatedAtDesc(Pet pet);

    /**
     * Count pet images for a pet
     * @param pet the pet
     * @return number of images for the pet
     */
    long countByPet(Pet pet);

    /**
     * Count pet images for a pet by pet ID
     * @param petId the pet ID
     * @return number of images for the pet
     */
    long countByPetId(UUID petId);

    /**
     * Check if a pet has a primary image
     * @param pet the pet
     * @return true if the pet has a primary image
     */
    boolean hasPrimaryImage(Pet pet);

    /**
     * Check if a pet has a primary image by pet ID
     * @param petId the pet ID
     * @return true if the pet has a primary image
     */
    boolean hasPrimaryImage(UUID petId);

    /**
     * Find pet images by pet ID with pagination
     * @param petId the pet ID
     * @param pageable pagination information
     * @return page of pet images for the pet
     */
    Page<PetImage> findByPetId(UUID petId, Pageable pageable);

    /**
     * Find primary image for a pet by pet ID
     * @param petId the pet ID
     * @return optional containing the primary image if found
     */
    Optional<PetImage> findPrimaryByPetId(UUID petId);

    /**
     * Find secondary (non-primary) images for a pet by pet ID
     * @param petId the pet ID
     * @return list of secondary images for the pet
     */
    List<PetImage> findSecondaryByPetId(UUID petId);

    /**
     * Find secondary (non-primary) images for a pet by pet ID with pagination
     * @param petId the pet ID
     * @param pageable pagination information
     * @return page of secondary images for the pet
     */
    Page<PetImage> findSecondaryByPetId(UUID petId, Pageable pageable);

    /**
     * Find pet images by image URL containing pattern
     * @param urlPattern the URL pattern to search for
     * @return list of pet images with URLs containing the pattern
     */
    List<PetImage> findByImageUrlContaining(String urlPattern);

    /**
     * Find pet images by image URL containing pattern with pagination
     * @param urlPattern the URL pattern to search for
     * @param pageable pagination information
     * @return page of pet images with URLs containing the pattern
     */
    Page<PetImage> findByImageUrlContaining(String urlPattern, Pageable pageable);

    /**
     * Remove primary status from an image
     * @param petImageId the pet image ID
     * @return the updated pet image
     */
    PetImage removePrimaryStatus(UUID petImageId);

    /**
     * Get total count of pet images
     * @return total count of pet images
     */
    long count();

    /**
     * Set an image as primary for a pet (removes primary flag from other images)
     * @param petImageId the pet image ID to set as primary
     * @return the updated pet image
     */
    PetImage setPrimaryImage(UUID petImageId);

    /**
     * Delete pet image by ID
     * @param id the pet image ID
     */
    void deleteById(UUID id);

    /**
     * Delete all pet images for a pet
     * @param pet the pet
     */
    void deleteByPet(Pet pet);

    /**
     * Delete all pet images for a pet by pet ID
     * @param petId the pet ID
     */
    void deleteByPetId(UUID petId);

    /**
     * Check if pet image exists by ID
     * @param id the pet image ID
     * @return true if the pet image exists
     */
    boolean existsById(UUID id);

    /**
     * Get pet image statistics
     * @return pet image statistics
     */
    PetImageStatistics getStatistics();

    /**
     * Get pet image statistics by pet
     * @param petId the pet ID
     * @return pet image statistics for the pet
     */
    PetImageStatistics getStatisticsByPet(UUID petId);

    /**
     * Inner class for pet image statistics
     */
    class PetImageStatistics {
        private final long totalImages;
        private final long primaryImages;
        private final long imagesWithoutPet;
        private final long averageImagesPerPet;

        public PetImageStatistics(long totalImages, long primaryImages, long imagesWithoutPet, long averageImagesPerPet) {
            this.totalImages = totalImages;
            this.primaryImages = primaryImages;
            this.imagesWithoutPet = imagesWithoutPet;
            this.averageImagesPerPet = averageImagesPerPet;
        }

        public long getTotalImages() { return totalImages; }
        public long getPrimaryImages() { return primaryImages; }
        public long getImagesWithoutPet() { return imagesWithoutPet; }
        public long getAverageImagesPerPet() { return averageImagesPerPet; }
    }
}
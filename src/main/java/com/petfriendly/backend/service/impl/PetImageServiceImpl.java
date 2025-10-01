package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetImage;
import com.petfriendly.backend.repository.PetImageRepository;
import com.petfriendly.backend.service.PetImageService;
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
 * Implementation of PetImageService interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PetImageServiceImpl implements PetImageService {

    // Manual logger since Lombok @Slf4j is not working
private final PetImageRepository petImageRepository;

    @Override
    public PetImage createPetImage(PetImage petImage) {
        log.debug("Creating new pet image for pet ID: {}", petImage.getPet().getId());
        
        petImage.setCreatedAt(LocalDateTime.now());
        petImage.setUpdatedAt(LocalDateTime.now());
        
        PetImage savedPetImage = petImageRepository.save(petImage);
        log.info("Pet image created successfully with ID: {}", savedPetImage.getId());
        
        return savedPetImage;
    }

    @Override
    public PetImage updatePetImage(UUID id, PetImage petImage) {
        log.debug("Updating pet image with ID: {}", id);
        
        PetImage existingPetImage = petImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet image not found with ID: " + id));
        
        // Update fields
        existingPetImage.setImageUrl(petImage.getImageUrl());
        existingPetImage.setAltText(petImage.getAltText());
        existingPetImage.setIsPrimary(petImage.getIsPrimary());
        existingPetImage.setUpdatedAt(LocalDateTime.now());
        
        PetImage updatedPetImage = petImageRepository.save(existingPetImage);
        log.info("Pet image updated successfully with ID: {}", updatedPetImage.getId());
        
        return updatedPetImage;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetImage> findById(UUID id) {
        log.debug("Finding pet image by ID: {}", id);
        return petImageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findAll() {
        log.debug("Finding all pet images");
        return petImageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetImage> findAll(Pageable pageable) {
        log.debug("Finding all pet images with pagination");
        return petImageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findByPet(Pet pet) {
        log.debug("Finding pet images by pet ID: {}", pet.getId());
        return petImageRepository.findByPet(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findByPetId(UUID petId) {
        log.debug("Finding pet images by pet ID: {}", petId);
        return petImageRepository.findByPetId(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetImage> findByPetId(UUID petId, Pageable pageable) {
        log.debug("Finding pet images by pet ID with pagination: {}", petId);
        return petImageRepository.findByPetId(petId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetImage> findPrimaryByPetId(UUID petId) {
        log.debug("Finding primary image for pet ID: {}", petId);
        return petImageRepository.findByPetIdAndIsPrimaryTrue(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findSecondaryByPetId(UUID petId) {
        log.debug("Finding secondary images for pet ID: {}", petId);
        return petImageRepository.findByPetIdAndIsPrimaryFalse(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetImage> findSecondaryByPetId(UUID petId, Pageable pageable) {
        log.debug("Finding secondary images for pet ID with pagination: {}", petId);
        return petImageRepository.findByPetIdAndIsPrimaryFalse(petId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findByImageUrlContaining(String urlPattern) {
        log.debug("Finding pet images by URL pattern: {}", urlPattern);
        return petImageRepository.findByImageUrlContainingIgnoreCase(urlPattern);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetImage> findByImageUrlContaining(String urlPattern, Pageable pageable) {
        log.debug("Finding pet images by URL pattern with pagination: {}", urlPattern);
        return petImageRepository.findByImageUrlContainingIgnoreCase(urlPattern, pageable);
    }

    @Override
    public PetImage removePrimaryStatus(UUID petImageId) {
        log.debug("Removing primary status from pet image: {}", petImageId);
        
        PetImage petImage = petImageRepository.findById(petImageId)
                .orElseThrow(() -> new RuntimeException("Pet image not found with ID: " + petImageId));
        
        petImage.setIsPrimary(false);
        petImage.setUpdatedAt(LocalDateTime.now());
        
        PetImage updatedImage = petImageRepository.save(petImage);
        log.info("Primary status removed from pet image: {}", petImageId);
        
        return updatedImage;
    }

    @Override
    public PetImage setPrimaryImage(UUID petImageId) {
        log.debug("Setting pet image as primary: {}", petImageId);
        
        PetImage petImage = petImageRepository.findById(petImageId)
                .orElseThrow(() -> new RuntimeException("Pet image not found with ID: " + petImageId));
        
        // First, remove primary status from any existing primary image of the same pet
        Optional<PetImage> existingPrimaryImage = petImageRepository.findByPetIdAndIsPrimaryTrue(petImage.getPet().getId());
        if (existingPrimaryImage.isPresent()) {
            PetImage existingPrimary = existingPrimaryImage.get();
            existingPrimary.setIsPrimary(false);
            existingPrimary.setUpdatedAt(LocalDateTime.now());
            petImageRepository.save(existingPrimary);
        }
        
        // Set this image as primary
        petImage.setIsPrimary(true);
        petImage.setUpdatedAt(LocalDateTime.now());
        
        PetImage updatedImage = petImageRepository.save(petImage);
        log.info("Pet image set as primary: {}", petImageId);
        
        return updatedImage;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Getting total count of pet images");
        return petImageRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPrimaryImage(UUID petId) {
        log.debug("Checking if pet has primary image: {}", petId);
        return petImageRepository.existsByPetIdAndIsPrimaryTrue(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPrimaryImage(Pet pet) {
        log.debug("Checking if pet has primary image: {}", pet.getId());
        return petImageRepository.existsByPetAndIsPrimaryTrue(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetImage> findPrimaryImageByPet(Pet pet) {
        log.debug("Finding primary image for pet ID: {}", pet.getId());
        return petImageRepository.findByPetAndIsPrimaryTrue(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetImage> findPrimaryImageByPetId(UUID petId) {
        log.debug("Finding primary image for pet ID: {}", petId);
        return petImageRepository.findByPetIdAndIsPrimaryTrue(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findAllPrimaryImages() {
        log.debug("Finding all primary images");
        return petImageRepository.findByIsPrimaryTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetImage> findByPetOrderByCreatedAtDesc(Pet pet) {
        log.debug("Finding pet images ordered by creation date for pet ID: {}", pet.getId());
        return petImageRepository.findByPetOrderByCreatedAtDesc(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        log.debug("Checking if pet image exists with ID: {}", id);
        return petImageRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPet(Pet pet) {
        log.debug("Counting pet images for pet ID: {}", pet.getId());
        return petImageRepository.countByPet(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPetId(UUID petId) {
        log.debug("Counting pet images for pet ID: {}", petId);
        return petImageRepository.countByPetId(petId);
    }

    @Override
    @Transactional(readOnly = true)
    public PetImageStatistics getStatistics() {
        log.debug("Getting pet image statistics");
        
        long totalImages = petImageRepository.count();
        long primaryImages = petImageRepository.countByIsPrimaryTrue();
        long imagesWithoutPet = petImageRepository.countByPetIsNull();
        
        // Calculate average images per pet
        long totalPets = petImageRepository.countDistinctPets();
        long averageImagesPerPet = totalPets > 0 ? totalImages / totalPets : 0;
        
        return new PetImageStatistics(totalImages, primaryImages, imagesWithoutPet, averageImagesPerPet);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Deleting pet image with ID: {}", id);
        
        if (!petImageRepository.existsById(id)) {
            throw new RuntimeException("Pet image not found with ID: " + id);
        }
        
        petImageRepository.deleteById(id);
        log.info("Pet image deleted successfully with ID: {}", id);
    }

    @Override
    public void deleteByPet(Pet pet) {
        log.info("Deleting all images for pet ID: {}", pet.getId());
        petImageRepository.deleteByPet(pet);
        log.info("All images deleted successfully for pet ID: {}", pet.getId());
    }

    @Override
    public void deleteByPetId(UUID petId) {
        log.info("Deleting all images for pet ID: {}", petId);
        petImageRepository.deleteByPetId(petId);
        log.info("All images deleted successfully for pet ID: {}", petId);
    }

    @Override
    @Transactional(readOnly = true)
    public PetImageStatistics getStatisticsByPet(UUID petId) {
        log.debug("Getting pet image statistics for pet ID: {}", petId);
        
        long totalImages = petImageRepository.countByPetId(petId);
        long primaryImages = petImageRepository.countByPetIdAndIsPrimaryTrue(petId);
        
        return new PetImageStatistics(totalImages, primaryImages, 0L, totalImages);
    }
}
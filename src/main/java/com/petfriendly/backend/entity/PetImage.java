package com.petfriendly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PetImage entity representing images associated with pets
 */
@Entity
@Table(name = "pet_images", indexes = {
    @Index(name = "idx_pet_images_pet", columnList = "pet_id"),
    @Index(name = "idx_pet_images_is_primary", columnList = "is_primary")
})
public class PetImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "image_url", nullable = false)
    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "alt_text")
    @Size(max = 255, message = "Alt text must not exceed 255 characters")
    private String altText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull(message = "Pet is required")
    private Pet pet;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public PetImage() {}

    public PetImage(String imageUrl, Pet pet) {
        this.imageUrl = imageUrl;
        this.pet = pet;
    }

    public PetImage(String imageUrl, Boolean isPrimary, Pet pet) {
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.pet = pet;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetImage petImage)) return false;
        return id != null && id.equals(petImage.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PetImage{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", isPrimary=" + isPrimary +
                ", createdAt=" + createdAt +
                '}';
    }
}
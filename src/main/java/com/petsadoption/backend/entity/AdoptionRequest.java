package com.petsadoption.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una solicitud de adopción de una mascota.
 * Contiene la información del adoptante y el estado de la solicitud.
 */
@Entity
@Table(name = "adoption_requests", indexes = {
    @Index(name = "idx_adoption_requests_pet_id", columnList = "pet_id"),
    @Index(name = "idx_adoption_requests_user_id", columnList = "user_id"),
    @Index(name = "idx_adoption_requests_status", columnList = "status"),
    @Index(name = "idx_adoption_requests_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @ToString.Exclude
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.SUBMITTED;

    @Column(name = "answers_json", columnDefinition = "TEXT")
    private String answersJson;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Enum para el tipo de vivienda del adoptante.
     */
    public enum HousingType {
        HOUSE("house"),
        APARTMENT("apartment"),
        FARM("farm"),
        OTHER("other");

        private final String value;

        HousingType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Enum para el estado de la solicitud de adopción.
     */
    public enum Status {
        SUBMITTED("submitted"),
        IN_REVIEW("in_review"),
        APPROVED("approved"),
        REJECTED("rejected"),
        CANCELLED("cancelled");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Método de conveniencia para aprobar la solicitud.
     */
    public void approve(UUID reviewerId, String notes) {
        this.status = Status.APPROVED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.notes = notes;
    }

    /**
     * Método de conveniencia para rechazar la solicitud.
     */
    public void reject(UUID reviewerId, String notes) {
        this.status = Status.REJECTED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.notes = notes;
    }

    /**
     * Método de conveniencia para poner en revisión.
     */
    public void putInReview(UUID reviewerId) {
        this.status = Status.IN_REVIEW;
        this.reviewedBy = reviewerId;
        this.reviewedAt = LocalDateTime.now();
    }

    /**
     * Método de conveniencia para cancelar la solicitud.
     */
    public void cancel() {
        this.status = Status.CANCELLED;
    }

    /**
     * Método de conveniencia para verificar si está pendiente.
     */
    public boolean isPending() {
        return Status.SUBMITTED.equals(this.status) || Status.IN_REVIEW.equals(this.status);
    }

    /**
     * Método de conveniencia para verificar si está aprobada.
     */
    public boolean isApproved() {
        return Status.APPROVED.equals(this.status);
    }

    /**
     * Método de conveniencia para verificar si está rechazada.
     */
    public boolean isRejected() {
        return Status.REJECTED.equals(this.status);
    }
    
    // Manual getters and setters for compatibility
    public UUID getId() {
        return this.id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Pet getPet() {
        return this.pet;
    }
    
    public void setPet(Pet pet) {
        this.pet = pet;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(UUID reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Manual builder method for compatibility
    public static AdoptionRequestBuilder builder() {
        return new AdoptionRequestBuilder();
    }
    
    public static class AdoptionRequestBuilder {
        private UUID id;
        private Pet pet;
        private User user;
        private Status status = Status.SUBMITTED;
        private String answersJson;
        private String notes;
        private UUID reviewedBy;
        private LocalDateTime reviewedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public AdoptionRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public AdoptionRequestBuilder pet(Pet pet) {
            this.pet = pet;
            return this;
        }
        
        public AdoptionRequestBuilder user(User user) {
            this.user = user;
            return this;
        }
        
        public AdoptionRequestBuilder status(Status status) {
            this.status = status;
            return this;
        }
        
        public AdoptionRequestBuilder answersJson(String answersJson) {
            this.answersJson = answersJson;
            return this;
        }
        
        public AdoptionRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }
        
        public AdoptionRequestBuilder reviewedBy(UUID reviewedBy) {
            this.reviewedBy = reviewedBy;
            return this;
        }
        
        public AdoptionRequestBuilder reviewedAt(LocalDateTime reviewedAt) {
            this.reviewedAt = reviewedAt;
            return this;
        }
        
        public AdoptionRequestBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public AdoptionRequestBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public AdoptionRequest build() {
            AdoptionRequest request = new AdoptionRequest();
            request.id = this.id;
            request.pet = this.pet;
            request.user = this.user;
            request.status = this.status;
            request.answersJson = this.answersJson;
            request.notes = this.notes;
            request.reviewedBy = this.reviewedBy;
            request.reviewedAt = this.reviewedAt;
            request.createdAt = this.createdAt;
            request.updatedAt = this.updatedAt;
            return request;
        }
    }
}
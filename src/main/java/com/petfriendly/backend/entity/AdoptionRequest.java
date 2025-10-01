package com.petfriendly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AdoptionRequest entity representing adoption requests from users.
 */
@Entity
@Table(name = "adoption_requests", indexes = {
        @Index(name = "idx_adoption_requests_user", columnList = "user_id"),
        @Index(name = "idx_adoption_requests_pet", columnList = "pet_id"),
        @Index(name = "idx_adoption_requests_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull(message = "Pet is required")
    private Pet pet;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String message;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private AdoptionRequestStatus status = AdoptionRequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(name = "living_situation", columnDefinition = "TEXT")
    private String livingSituation;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdoptionRequest that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AdoptionRequest{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
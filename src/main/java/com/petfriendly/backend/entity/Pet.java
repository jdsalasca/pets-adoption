package com.petfriendly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pet entity representing pets available for adoption.
 */
@Entity
@Table(name = "pets", indexes = {
        @Index(name = "idx_pets_species", columnList = "species"),
        @Index(name = "idx_pets_status", columnList = "status"),
        @Index(name = "idx_pets_foundation", columnList = "foundation_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Pet name is required")
    @Size(max = 100, message = "Pet name must not exceed 100 characters")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @NotNull(message = "Species is required")
    private PetSpecies species;

    @Size(max = 100, message = "Breed must not exceed 100 characters")
    private String breed;

    @Positive(message = "Age must be positive")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PetGender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PetSize size;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private PetStatus status = PetStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id", nullable = false)
    @NotNull(message = "Foundation is required")
    private Foundation foundation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PetImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AdoptionRequest> adoptionRequests = new ArrayList<>();

    // Helper methods
    public void addImage(PetImage image) {
        images.add(image);
        image.setPet(this);
    }

    public void removeImage(PetImage image) {
        images.remove(image);
        image.setPet(null);
    }

    public void addAdoptionRequest(AdoptionRequest request) {
        adoptionRequests.add(request);
        request.setPet(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet pet)) return false;
        return id != null && id.equals(pet.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species=" + species +
                ", status=" + status +
                '}';
    }
}
package com.petsadoption.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una mascota dentro del dominio PETFRIENDLY.
 */
@Entity
@Table(name = "pets", indexes = {
        @Index(name = "idx_pets_foundation_id", columnList = "foundation_id"),
        @Index(name = "idx_pets_species", columnList = "species"),
        @Index(name = "idx_pets_status", columnList = "status"),
        @Index(name = "idx_pets_city", columnList = "city")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id", nullable = false)
    @ToString.Exclude
    private Foundation foundation;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @jakarta.validation.constraints.Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species species;

    @Column(name = "breed")
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @Column(name = "age_months", nullable = false)
    private Integer ageMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private Size size;

    @jakarta.validation.constraints.Size(max = 255, message = "El temperamento no puede exceder 255 caracteres")
    @Column(name = "temperament")
    private String temperament;

    @Column(name = "health", columnDefinition = "TEXT")
    private String health;

    @NotBlank(message = "La ciudad es obligatoria")
    @jakarta.validation.constraints.Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "city", nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.AVAILABLE;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<PetImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<AdoptionRequest> adoptionRequests = new ArrayList<>();

    public enum Species {
        DOG("dog"),
        CAT("cat"),
        OTHER("other");

        private final String value;

        Species(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Sex {
        MALE("male"),
        FEMALE("female");

        private final String value;

        Sex(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Size {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large");

        private final String value;

        Size(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Status {
        AVAILABLE("available"),
        RESERVED("reserved"),
        ADOPTED("adopted");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public void addImage(PetImage image) {
        images.add(image);
        image.setPet(this);
    }

    public void removeImage(PetImage image) {
        images.remove(image);
        image.setPet(null);
    }

    public void addAdoptionRequest(AdoptionRequest adoptionRequest) {
        adoptionRequests.add(adoptionRequest);
        adoptionRequest.setPet(this);
    }

    public boolean isAvailable() {
        return Status.AVAILABLE.equals(this.status);
    }

    public boolean isReserved() {
        return Status.RESERVED.equals(this.status);
    }

    public boolean isAdopted() {
        return Status.ADOPTED.equals(this.status);
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Foundation getFoundation() {
        return foundation;
    }

    public void setFoundation(Foundation foundation) {
        this.foundation = foundation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Integer getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(Integer ageMonths) {
        this.ageMonths = ageMonths;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<PetImage> getImages() {
        return images;
    }

    public void setImages(List<PetImage> images) {
        this.images = images;
    }

    public List<AdoptionRequest> getAdoptionRequests() {
        return adoptionRequests;
    }

    public void setAdoptionRequests(List<AdoptionRequest> adoptionRequests) {
        this.adoptionRequests = adoptionRequests;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

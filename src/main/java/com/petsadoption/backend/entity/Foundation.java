package com.petsadoption.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una fundación o refugio de mascotas.
 * Las fundaciones pueden registrar mascotas y gestionar solicitudes de adopción.
 */
@Entity
@Table(name = "foundations", indexes = {
    @Index(name = "idx_foundations_city", columnList = "city"),
    @Index(name = "idx_foundations_verified", columnList = "verified"),
    @Index(name = "idx_foundations_name", columnList = "name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foundation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El nombre de la fundación es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "El email de contacto es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El formato del teléfono no es válido")
    @Column(name = "phone")
    private String phone;

    @Column(name = "website")
    private String website;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private Boolean verified = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relaciones
    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<ContactMessage> contactMessages = new ArrayList<>();

    /**
     * Método de conveniencia para agregar una mascota.
     */
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setFoundation(this);
    }

    /**
     * Método de conveniencia para remover una mascota.
     */
    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setFoundation(null);
    }

    /**
     * Método de conveniencia para agregar un mensaje de contacto.
     */
    public void addContactMessage(ContactMessage contactMessage) {
        contactMessages.add(contactMessage);
        // La relación bidireccional se maneja a nivel de servicio
    }

    /**
     * Método de conveniencia para obtener el número de mascotas disponibles.
     */
    public long getAvailablePetsCount() {
        return pets.stream()
                .filter(pet -> Pet.Status.AVAILABLE.equals(pet.getStatus()))
                .count();
    }

    /**
     * Método de conveniencia para obtener el número de mascotas adoptadas.
     */
    public long getAdoptedPetsCount() {
        return pets.stream()
                .filter(pet -> Pet.Status.ADOPTED.equals(pet.getStatus()))
                .count();
    }

    /**
     * Método de conveniencia para verificar si la fundación está verificada y activa.
     */
    public boolean isVerifiedAndActive() {
        return Boolean.TRUE.equals(verified) && Boolean.TRUE.equals(isActive);
    }
    
    // Manual getter for name to ensure compatibility
    public String getName() {
        return this.name;
    }
    
    // Manual setter for name to ensure compatibility
    public void setName(String name) {
        this.name = name;
    }
    
    // Manual getter for id to ensure compatibility
    public UUID getId() {
        return this.id;
    }
    
    // Manual setter for id to ensure compatibility
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
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
 * Entidad que representa un usuario del sistema.
 * Esta tabla complementa la tabla auth.users de Supabase con información adicional
 * específica de la aplicación PETFRIENDLY.
 * 
 * IMPORTANTE: Esta entidad referencia auth.users de Supabase mediante UUID.
 * El ID debe coincidir con el user_id de auth.users.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_role", columnList = "role"),
    @Index(name = "idx_users_city", columnList = "city")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * ID del usuario que debe coincidir con auth.users.id de Supabase.
     * No se genera automáticamente, se asigna desde el JWT de Supabase.
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El formato del teléfono no es válido")
    @Column(name = "phone")
    private String phone;

    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Column(name = "city")
    private String city;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.ADOPTER;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<AdoptionRequest> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<ContactMessage> contactMessages = new ArrayList<>();

    /**
     * Enum para los roles de usuario en el sistema.
     */
    public enum Role {
        ADOPTER("adopter"),
        FOUNDATION_ADMIN("foundation_admin"),
        SUPER_ADMIN("super_admin");

        private final String value;

        Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Método de conveniencia para obtener el nombre completo.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Método de conveniencia para verificar si es administrador de fundación.
     */
    public boolean isFoundationAdmin() {
        return Role.FOUNDATION_ADMIN.equals(this.role);
    }

    /**
     * Método de conveniencia para verificar si es super administrador.
     */
    public boolean isSuperAdmin() {
        return Role.SUPER_ADMIN.equals(this.role);
    }

    /**
     * Gets the user's role
     * @return the user's role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Checks if the user is an adopter
     * @return true if the user is an adopter, false otherwise
     */
    public boolean isAdopter() {
        return this.role == Role.ADOPTER;
    }

    /**
     * Método de conveniencia para agregar una solicitud de adopción.
     */
    public void addAdoptionRequest(AdoptionRequest adoptionRequest) {
        adoptionRequests.add(adoptionRequest);
        // La relación bidireccional se maneja a nivel de servicio
    }

    /**
     * Método de conveniencia para agregar un mensaje de contacto.
     */
    public void addContactMessage(ContactMessage contactMessage) {
        contactMessages.add(contactMessage);
        // La relación bidireccional se maneja a nivel de servicio
    }
    
    // Manual getter for email to ensure compatibility
    public String getEmail() {
        return this.email;
    }
    
    // Manual setter for email to ensure compatibility
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Manual getter for id to ensure compatibility
    public UUID getId() {
        return this.id;
    }
    
    // Manual setter for id to ensure compatibility
    public void setId(UUID id) {
        this.id = id;
    }

    // Getters manuales adicionales
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getPhone() {
        return this.phone;
    }
}
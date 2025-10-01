package com.petfriendly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Foundation entity representing pet adoption foundations.
 */
@Entity
@Table(name = "foundations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Foundation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Foundation name is required")
    @Size(max = 255, message = "Foundation name must not exceed 255 characters")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "contact_email", nullable = false)
    @Email(message = "Contact email should be valid")
    @NotBlank(message = "Contact email is required")
    @Size(max = 255, message = "Contact email must not exceed 255 characters")
    private String contactEmail;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone_number")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    private Boolean verified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pet> pets = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "foundation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContactMessage> contactMessages = new ArrayList<>();

    // Helper methods
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setFoundation(this);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setFoundation(null);
    }

    public void addContactMessage(ContactMessage message) {
        contactMessages.add(message);
        message.setFoundation(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Foundation foundation)) return false;
        return id != null && id.equals(foundation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Foundation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", verified=" + verified +
                '}';
    }
}
package com.petsadoption.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un mensaje de contacto enviado a una fundación.
 * Permite a los usuarios contactar directamente con las fundaciones.
 */
@Entity
@Table(name = "contact_messages", indexes = {
    @Index(name = "idx_contact_messages_foundation_id", columnList = "foundation_id"),
    @Index(name = "idx_contact_messages_user_id", columnList = "user_id"),
    @Index(name = "idx_contact_messages_created_at", columnList = "created_at"),
    @Index(name = "idx_contact_messages_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id", nullable = false)
    @ToString.Exclude
    private Foundation foundation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "from_name", nullable = false)
    private String fromName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "El formato del teléfono no es válido")
    @Column(name = "phone")
    private String phone;

    @Size(max = 200, message = "El asunto no puede exceder 200 caracteres")
    @Column(name = "subject")
    private String subject;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 2000, message = "El mensaje no puede exceder 2000 caracteres")
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.UNREAD;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    @Builder.Default
    private MessageType messageType = MessageType.GENERAL;

    @Column(name = "pet_id")
    private UUID petId;

    @Column(name = "response", columnDefinition = "TEXT")
    private String response;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Column(name = "responded_by")
    private UUID respondedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Enum para el estado del mensaje de contacto.
     */
    public enum Status {
        UNREAD("unread"),
        READ("read"),
        RESPONDED("responded"),
        ARCHIVED("archived");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Enum para el tipo de mensaje de contacto.
     */
    public enum MessageType {
        GENERAL("general"),
        ADOPTION_INQUIRY("adoption_inquiry"),
        VOLUNTEER("volunteer"),
        DONATION("donation"),
        COMPLAINT("complaint"),
        SUGGESTION("suggestion");

        private final String value;

        MessageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Método de conveniencia para marcar como leído.
     */
    public void markAsRead() {
        this.status = Status.READ;
    }

    /**
     * Método de conveniencia para marcar como respondido.
     */
    public void markAsResponded(String response, UUID respondedBy) {
        this.status = Status.RESPONDED;
        this.response = response;
        this.respondedBy = respondedBy;
        this.respondedAt = LocalDateTime.now();
    }

    /**
     * Método de conveniencia para archivar el mensaje.
     */
    public void archive() {
        this.status = Status.ARCHIVED;
    }

    /**
     * Método de conveniencia para verificar si el mensaje no ha sido leído.
     */
    public boolean isUnread() {
        return Status.UNREAD.equals(this.status);
    }

    /**
     * Método de conveniencia para verificar si el mensaje ha sido respondido.
     */
    public boolean isResponded() {
        return Status.RESPONDED.equals(this.status);
    }

    /**
     * Método de conveniencia para verificar si es una consulta sobre adopción.
     */
    public boolean isAdoptionInquiry() {
        return MessageType.ADOPTION_INQUIRY.equals(this.messageType);
    }

    /**
     * Método de conveniencia para obtener el nombre del remitente o el nombre del usuario.
     */
    public String getSenderName() {
        if (user != null) {
            return user.getFullName();
        }
        return fromName;
    }

    /**
     * Método de conveniencia para obtener el email del remitente o el email del usuario.
     */
    public String getSenderEmail() {
        if (user != null) {
            return user.getEmail();
        }
        return fromEmail;
    }
}
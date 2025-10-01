package com.petfriendly.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ContactMessage entity representing contact messages sent to foundations.
 */
@Entity
@Table(name = "contact_messages", indexes = {
        @Index(name = "idx_contact_messages_foundation", columnList = "foundation_id"),
        @Index(name = "idx_contact_messages_email", columnList = "sender_email")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sender_name", nullable = false)
    @NotBlank(message = "Sender name is required")
    @Size(max = 255, message = "Sender name must not exceed 255 characters")
    private String senderName;

    @Column(name = "sender_email", nullable = false)
    @Email(message = "Sender email should be valid")
    @NotBlank(message = "Sender email is required")
    @Size(max = 255, message = "Sender email must not exceed 255 characters")
    private String senderEmail;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foundation_id", nullable = false)
    @NotNull(message = "Foundation is required")
    private Foundation foundation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "subject")
    @Size(max = 255, message = "Subject must not exceed 255 characters")
    private String subject;
}
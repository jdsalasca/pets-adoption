package com.petfriendly.backend.repository;

import com.petfriendly.backend.entity.ContactMessage;
import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for ContactMessage entity operations.
 *
 * The method signatures defined here are aligned with the requirements of
 * ContactMessageServiceImpl. Remove or consolidate any duplicates carefully
 * because Spring Data derives query implementations from the signatures.
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {

    List<ContactMessage> findByFoundation(Foundation foundation);

    Page<ContactMessage> findByFoundation(Foundation foundation, Pageable pageable);

    List<ContactMessage> findByFoundationId(UUID foundationId);

    Page<ContactMessage> findByFoundationId(UUID foundationId, Pageable pageable);

    List<ContactMessage> findBySenderEmail(String senderEmail);

    Page<ContactMessage> findBySenderEmail(String senderEmail, Pageable pageable);

    List<ContactMessage> findBySenderNameContainingIgnoreCase(String senderName);

    Page<ContactMessage> findBySenderNameContainingIgnoreCase(String senderName, Pageable pageable);

    List<ContactMessage> findBySubjectContainingIgnoreCase(String subject);

    Page<ContactMessage> findBySubjectContainingIgnoreCase(String subject, Pageable pageable);

    List<ContactMessage> findByMessageContainingIgnoreCase(String message);

    Page<ContactMessage> findByMessageContainingIgnoreCase(String message, Pageable pageable);

    List<ContactMessage> findByIsRead(boolean read);

    Page<ContactMessage> findByIsRead(boolean read, Pageable pageable);

    List<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean read);

    Page<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean read, Pageable pageable);

    List<ContactMessage> findByFoundationIdAndIsReadFalse(UUID foundationId);

    Page<ContactMessage> findByFoundationIdAndIsReadFalse(UUID foundationId, Pageable pageable);

    long countByFoundation(Foundation foundation);

    long countByFoundationId(UUID foundationId);

    long countByFoundationIdAndIsRead(UUID foundationId, boolean read);

    long countByFoundationIdAndIsReadFalse(UUID foundationId);

    long countByFoundationIdAndIsReadTrue(UUID foundationId);

    long countByIsRead(boolean read);

    long countByIsReadFalse();

    long countByIsReadTrue();

    long countByCreatedAtAfter(LocalDateTime date);

    long countByFoundationIdAndCreatedAtAfter(UUID foundationId, LocalDateTime date);

    Page<ContactMessage> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<ContactMessage> findByFoundationIdAndCreatedAtAfter(UUID foundationId, LocalDateTime date, Pageable pageable);

    boolean existsByFoundationId(UUID foundationId);

    /**
     * Deletes all contact messages for a specific foundation.
     *
     * @param foundation the foundation entity
     */
    void deleteByFoundation(Foundation foundation);

    /**
     * Deletes all contact messages for a specific foundation ID.
     *
     * @param foundationId the foundation ID
     */
    void deleteByFoundationId(UUID foundationId);
}
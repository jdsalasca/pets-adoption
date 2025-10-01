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

    List<ContactMessage> findByFoundation_Id(UUID foundationId);

    Page<ContactMessage> findByFoundation_Id(UUID foundationId, Pageable pageable);

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

    List<ContactMessage> findByFoundation_IdAndIsRead(UUID foundationId, boolean read);

    Page<ContactMessage> findByFoundation_IdAndIsRead(UUID foundationId, boolean read, Pageable pageable);

    List<ContactMessage> findByFoundation_IdAndIsReadFalse(UUID foundationId);

    Page<ContactMessage> findByFoundation_IdAndIsReadFalse(UUID foundationId, Pageable pageable);

    long countByFoundation(Foundation foundation);

    long countByFoundation_Id(UUID foundationId);

    long countByFoundation_IdAndIsRead(UUID foundationId, boolean read);

    long countByFoundation_IdAndIsReadFalse(UUID foundationId);

    long countByFoundation_IdAndIsReadTrue(UUID foundationId);

    long countByIsRead(boolean read);

    long countByIsReadFalse();

    long countByIsReadTrue();

    long countByCreatedAtAfter(LocalDateTime date);

    long countByFoundation_IdAndCreatedAtAfter(UUID foundationId, LocalDateTime date);

    Page<ContactMessage> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<ContactMessage> findByFoundation_IdAndCreatedAtAfter(UUID foundationId, LocalDateTime date, Pageable pageable);

    boolean existsByFoundation_Id(UUID foundationId);

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
    void deleteByFoundation_Id(UUID foundationId);
}
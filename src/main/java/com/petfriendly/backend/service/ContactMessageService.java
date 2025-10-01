package com.petfriendly.backend.service;

import com.petfriendly.backend.entity.ContactMessage;
import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for ContactMessage entity operations
 */
public interface ContactMessageService {

    /**
     * Create a new contact message
     * @param contactMessage the contact message to create
     * @return the created contact message
     */
    ContactMessage createContactMessage(ContactMessage contactMessage);

    /**
     * Update an existing contact message
     * @param id the contact message ID
     * @param contactMessage the updated contact message data
     * @return the updated contact message
     */
    ContactMessage updateContactMessage(UUID id, ContactMessage contactMessage);

    /**
     * Mark contact message as read
     * @param id the contact message ID
     * @return the updated contact message
     */
    ContactMessage markAsRead(UUID id);

    /**
     * Mark contact message as unread
     * @param id the contact message ID
     * @return the updated contact message
     */
    ContactMessage markAsUnread(UUID id);

    /**
     * Find contact message by ID
     * @param id the contact message ID
     * @return optional containing the contact message if found
     */
    Optional<ContactMessage> findById(UUID id);

    /**
     * Find all contact messages
     * @return list of all contact messages
     */
    List<ContactMessage> findAll();

    /**
     * Find all contact messages with pagination
     * @param pageable pagination information
     * @return page of contact messages
     */
    Page<ContactMessage> findAll(Pageable pageable);

    /**
     * Find contact messages by foundation
     * @param foundation the foundation
     * @return list of contact messages for the foundation
     */
    List<ContactMessage> findByFoundation(Foundation foundation);

    /**
     * Find contact messages by foundation with pagination
     * @param foundation the foundation
     * @param pageable pagination information
     * @return page of contact messages for the foundation
     */
    Page<ContactMessage> findByFoundation(Foundation foundation, Pageable pageable);

    /**
     * Find contact messages by foundation ID
     * @param foundationId the foundation ID
     * @return list of contact messages for the foundation
     */
    List<ContactMessage> findByFoundationId(UUID foundationId);

    /**
     * Find contact messages by foundation ID with pagination
     * @param foundationId the foundation ID
     * @param pageable pagination information
     * @return page of contact messages for the foundation
     */
    Page<ContactMessage> findByFoundationId(UUID foundationId, Pageable pageable);

    /**
     * Find contact messages by sender email
     * @param senderEmail the sender email
     * @return list of contact messages from the sender
     */
    List<ContactMessage> findBySenderEmail(String senderEmail);

    /**
     * Find contact messages by sender email with pagination
     * @param senderEmail the sender email
     * @param pageable pagination information
     * @return page of contact messages from the sender
     */
    Page<ContactMessage> findBySenderEmail(String senderEmail, Pageable pageable);

    /**
     * Find contact messages by sender email (alias for findBySenderEmail)
     * @param email the sender email
     * @return list of contact messages from the sender
     */
    List<ContactMessage> findByEmail(String email);

    /**
     * Find contact messages by sender email with pagination (alias for findBySenderEmail)
     * @param email the sender email
     * @param pageable pagination information
     * @return page of contact messages from the sender
     */
    Page<ContactMessage> findByEmail(String email, Pageable pageable);

    /**
     * Find contact messages by sender name containing pattern
     * @param name the name pattern
     * @return list of contact messages with matching sender names
     */
    List<ContactMessage> findByNameContaining(String name);

    /**
     * Find contact messages by sender name containing pattern with pagination
     * @param name the name pattern
     * @param pageable pagination information
     * @return page of contact messages with matching sender names
     */
    Page<ContactMessage> findByNameContaining(String name, Pageable pageable);

    /**
     * Find contact messages by subject containing pattern
     * @param subject the subject pattern
     * @return list of contact messages with matching subjects
     */
    List<ContactMessage> findBySubjectContaining(String subject);

    /**
     * Find contact messages by subject containing pattern with pagination
     * @param subject the subject pattern
     * @param pageable pagination information
     * @return page of contact messages with matching subjects
     */
    Page<ContactMessage> findBySubjectContaining(String subject, Pageable pageable);

    /**
     * Find contact messages by message content containing pattern
     * @param message the message pattern
     * @return list of contact messages with matching message content
     */
    List<ContactMessage> findByMessageContaining(String message);

    /**
     * Find contact messages by message content containing pattern with pagination
     * @param message the message pattern
     * @param pageable pagination information
     * @return page of contact messages with matching message content
     */
    Page<ContactMessage> findByMessageContaining(String message, Pageable pageable);

    /**
     * Find contact messages by read status
     * @param isRead the read status
     * @return list of contact messages with the specified read status
     */
    List<ContactMessage> findByIsRead(boolean isRead);

    /**
     * Find contact messages by read status with pagination
     * @param isRead the read status
     * @param pageable pagination information
     * @return page of contact messages with the specified read status
     */
    Page<ContactMessage> findByIsRead(boolean isRead, Pageable pageable);

    /**
     * Find contact messages by foundation ID and read status
     * @param foundationId the foundation ID
     * @param isRead the read status
     * @return list of contact messages for the foundation with the specified read status
     */
    List<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean isRead);

    /**
     * Find contact messages by foundation ID and read status with pagination
     * @param foundationId the foundation ID
     * @param isRead the read status
     * @param pageable pagination information
     * @return page of contact messages for the foundation with the specified read status
     */
    Page<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean isRead, Pageable pageable);

    /**
     * Count all contact messages
     * @return total number of contact messages
     */
    long count();

    /**
     * Count contact messages by read status
     * @param isRead the read status
     * @return number of contact messages with the specified read status
     */
    long countByIsRead(boolean isRead);

    /**
     * Find unread contact messages for a foundation
     * @param foundationId the foundation ID
     * @param pageable pagination information
     * @return page of unread contact messages
     */
    Page<ContactMessage> findUnreadByFoundation(UUID foundationId, Pageable pageable);

    /**
     * Find contact messages by date range
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of contact messages within the date range
     */
    Page<ContactMessage> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find recent contact messages for a foundation
     * @param foundationId the foundation ID
     * @param pageable pagination information
     * @return page of recent contact messages
     */
    Page<ContactMessage> findRecentByFoundation(UUID foundationId, Pageable pageable);

    /**
     * Count contact messages by foundation
     * @param foundation the foundation
     * @return number of contact messages for the foundation
     */
    long countByFoundation(Foundation foundation);

    /**
     * Count contact messages by foundation ID
     * @param foundationId the foundation ID
     * @return number of contact messages for the foundation
     */
    long countByFoundationId(UUID foundationId);

    /**
     * Count contact messages by foundation ID and read status
     * @param foundationId the foundation ID
     * @param isRead the read status
     * @return number of contact messages for the foundation with the specified read status
     */
    long countByFoundationIdAndIsRead(UUID foundationId, boolean isRead);

    /**
     * Count unread contact messages by foundation
     * @param foundation the foundation
     * @return number of unread contact messages for the foundation
     */
    long countUnreadByFoundation(Foundation foundation);

    /**
     * Count unread contact messages by foundation ID
     * @param foundationId the foundation ID
     * @return number of unread contact messages for the foundation
     */
    long countUnreadByFoundationId(UUID foundationId);

    /**
     * Check if contact message exists by ID
     * @param id the contact message ID
     * @return true if the contact message exists
     */
    boolean existsById(UUID id);

    /**
     * Delete contact message by ID
     * @param id the contact message ID
     */
    void deleteById(UUID id);

    /**
     * Delete all contact messages for a foundation
     * @param foundation the foundation
     */
    void deleteByFoundation(Foundation foundation);

    /**
     * Delete all contact messages for a foundation by foundation ID
     * @param foundationId the foundation ID
     */
    void deleteByFoundationId(UUID foundationId);

    /**
     * Get contact message statistics
     * @return contact message statistics
     */
    ContactMessageStatistics getStatistics();

    /**
     * Get contact message statistics for a foundation
     * @param foundationId the foundation ID
     * @return contact message statistics for the foundation
     */
    ContactMessageStatistics getStatisticsByFoundation(UUID foundationId);

    /**
     * Statistics class for contact messages
     */
    class ContactMessageStatistics {
        private final long totalMessages;
        private final long unreadMessages;
        private final long readMessages;
        private final long todayMessages;
        private final long weekMessages;
        private final long monthMessages;

        public ContactMessageStatistics(long totalMessages, long unreadMessages, 
                                      long readMessages, long todayMessages, 
                                      long weekMessages, long monthMessages) {
            this.totalMessages = totalMessages;
            this.unreadMessages = unreadMessages;
            this.readMessages = readMessages;
            this.todayMessages = todayMessages;
            this.weekMessages = weekMessages;
            this.monthMessages = monthMessages;
        }

        // Getters
        public long getTotalMessages() { return totalMessages; }
        public long getUnreadMessages() { return unreadMessages; }
        public long getReadMessages() { return readMessages; }
        public long getTodayMessages() { return todayMessages; }
        public long getWeekMessages() { return weekMessages; }
        public long getMonthMessages() { return monthMessages; }
    }
}
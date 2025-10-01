package com.petfriendly.backend.service.impl;

import com.petfriendly.backend.entity.ContactMessage;
import com.petfriendly.backend.entity.Foundation;
import com.petfriendly.backend.repository.ContactMessageRepository;
import com.petfriendly.backend.repository.FoundationRepository;
import com.petfriendly.backend.service.ContactMessageService;
import com.petfriendly.backend.service.ContactMessageService.ContactMessageStatistics;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of ContactMessageService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ContactMessageServiceImpl implements ContactMessageService {
    
    private static final Logger log = LoggerFactory.getLogger(ContactMessageServiceImpl.class);
    
    private final ContactMessageRepository contactMessageRepository;
    private final FoundationRepository foundationRepository;

    @Override
    public ContactMessage createContactMessage(ContactMessage contactMessage) {
        log.debug("Creating new contact message from: {}", contactMessage.getSenderEmail());
        contactMessage.setCreatedAt(LocalDateTime.now());
        contactMessage.setIsRead(false);
        return contactMessageRepository.save(contactMessage);
    }

    @Override
    public ContactMessage updateContactMessage(UUID id, ContactMessage contactMessage) {
        log.debug("Updating contact message with ID: {}", id);
        return contactMessageRepository.findById(id)
                .map(existingMessage -> {
                    existingMessage.setSenderName(contactMessage.getSenderName());
                    existingMessage.setSenderEmail(contactMessage.getSenderEmail());
                    existingMessage.setSubject(contactMessage.getSubject());
                    existingMessage.setMessage(contactMessage.getMessage());
                    return contactMessageRepository.save(existingMessage);
                })
                .orElseThrow(() -> new RuntimeException("Contact message not found with ID: " + id));
    }

    @Override
    public ContactMessage markAsRead(UUID id) {
        log.debug("Marking contact message as read: {}", id);
        return contactMessageRepository.findById(id)
                .map(message -> {
                    message.setIsRead(true);
                    message.setReadAt(LocalDateTime.now());
                    return contactMessageRepository.save(message);
                })
                .orElseThrow(() -> new RuntimeException("Contact message not found with ID: " + id));
    }

    @Override
    public ContactMessage markAsUnread(UUID id) {
        log.debug("Marking contact message as unread: {}", id);
        return contactMessageRepository.findById(id)
                .map(message -> {
                    message.setIsRead(false);
                    message.setReadAt(null);
                    return contactMessageRepository.save(message);
                })
                .orElseThrow(() -> new RuntimeException("Contact message not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactMessage> findById(UUID id) {
        log.debug("Finding contact message by ID: {}", id);
        return contactMessageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findAll() {
        log.debug("Finding all contact messages");
        return contactMessageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findAll(Pageable pageable) {
        log.debug("Finding all contact messages with pagination");
        return contactMessageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByFoundation(Foundation foundation) {
        log.debug("Finding contact messages by foundation: {}", foundation.getName());
        return contactMessageRepository.findByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByFoundation(Foundation foundation, Pageable pageable) {
        log.debug("Finding contact messages by foundation with pagination: {}", foundation.getName());
        return contactMessageRepository.findByFoundation(foundation, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByFoundationId(UUID foundationId) {
        log.debug("Finding contact messages by foundation ID: {}", foundationId);
        return contactMessageRepository.findByFoundation_Id(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByFoundationId(UUID foundationId, Pageable pageable) {
        log.debug("Finding contact messages by foundation ID with pagination: {}", foundationId);
        return contactMessageRepository.findByFoundation_Id(foundationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findBySenderEmail(String senderEmail) {
        log.debug("Finding contact messages by sender email: {}", senderEmail);
        return contactMessageRepository.findBySenderEmail(senderEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findBySenderEmail(String senderEmail, Pageable pageable) {
        log.debug("Finding contact messages by sender email with pagination: {}", senderEmail);
        return contactMessageRepository.findBySenderEmail(senderEmail, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByEmail(String email) {
        log.debug("Finding contact messages by email: {}", email);
        return contactMessageRepository.findBySenderEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByEmail(String email, Pageable pageable) {
        log.debug("Finding contact messages by email with pagination: {}", email);
        return contactMessageRepository.findBySenderEmail(email, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByNameContaining(String name) {
        log.debug("Finding contact messages by name containing: {}", name);
        return contactMessageRepository.findBySenderNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByNameContaining(String name, Pageable pageable) {
        log.debug("Finding contact messages by name containing with pagination: {}", name);
        return contactMessageRepository.findBySenderNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findBySubjectContaining(String subject) {
        log.debug("Finding contact messages by subject containing: {}", subject);
        return contactMessageRepository.findBySubjectContainingIgnoreCase(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findBySubjectContaining(String subject, Pageable pageable) {
        log.debug("Finding contact messages by subject containing with pagination: {}", subject);
        return contactMessageRepository.findBySubjectContainingIgnoreCase(subject, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByMessageContaining(String message) {
        log.debug("Finding contact messages by message containing: {}", message);
        return contactMessageRepository.findByMessageContainingIgnoreCase(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByMessageContaining(String message, Pageable pageable) {
        log.debug("Finding contact messages by message containing with pagination: {}", message);
        return contactMessageRepository.findByMessageContainingIgnoreCase(message, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByIsRead(boolean isRead) {
        log.debug("Finding contact messages by read status: {}", isRead);
        return contactMessageRepository.findByIsRead(isRead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByIsRead(boolean isRead, Pageable pageable) {
        log.debug("Finding contact messages by read status with pagination: {}", isRead);
        return contactMessageRepository.findByIsRead(isRead, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean isRead) {
        log.debug("Finding contact messages by foundation ID and read status: {} - {}", foundationId, isRead);
        return contactMessageRepository.findByFoundation_IdAndIsRead(foundationId, isRead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByFoundationIdAndIsRead(UUID foundationId, boolean isRead, Pageable pageable) {
        log.debug("Finding contact messages by foundation ID and read status with pagination: {} - {}", foundationId, isRead);
        return contactMessageRepository.findByFoundation_IdAndIsRead(foundationId, isRead, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        log.debug("Counting all contact messages");
        return contactMessageRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByIsRead(boolean isRead) {
        log.debug("Counting contact messages by read status: {}", isRead);
        return contactMessageRepository.countByIsRead(isRead);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findUnreadByFoundation(UUID foundationId, Pageable pageable) {
        log.debug("Finding unread contact messages by foundation ID: {}", foundationId);
        return contactMessageRepository.findByFoundation_IdAndIsReadFalse(foundationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Finding contact messages by date range: {} to {}", startDate, endDate);
        return contactMessageRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessage> findRecentByFoundation(UUID foundationId, Pageable pageable) {
        log.debug("Finding recent contact messages by foundation ID: {}", foundationId);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return contactMessageRepository.findByFoundation_IdAndCreatedAtAfter(foundationId, sevenDaysAgo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundation(Foundation foundation) {
        log.debug("Counting contact messages by foundation: {}", foundation.getName());
        return contactMessageRepository.countByFoundation(foundation);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundationId(UUID foundationId) {
        log.debug("Counting contact messages by foundation ID: {}", foundationId);
        return contactMessageRepository.countByFoundation_Id(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByFoundationIdAndIsRead(UUID foundationId, boolean isRead) {
        log.debug("Counting contact messages by foundation ID and read status: {} - {}", foundationId, isRead);
        if (isRead) {
            return contactMessageRepository.countByFoundation_IdAndIsReadTrue(foundationId);
        } else {
            return contactMessageRepository.countByFoundation_IdAndIsReadFalse(foundationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByFoundation(Foundation foundation) {
        log.debug("Counting unread contact messages by foundation: {}", foundation.getName());
        return contactMessageRepository.countByFoundation_IdAndIsReadFalse(foundation.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByFoundationId(UUID foundationId) {
        log.debug("Counting unread contact messages by foundation ID: {}", foundationId);
        return contactMessageRepository.countByFoundation_IdAndIsReadFalse(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        log.debug("Checking if contact message exists by ID: {}", id);
        return contactMessageRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting contact message by ID: {}", id);
        if (!contactMessageRepository.existsById(id)) {
            throw new RuntimeException("Contact message not found with ID: " + id);
        }
        contactMessageRepository.deleteById(id);
    }

    @Override
    public void deleteByFoundation(Foundation foundation) {
        log.debug("Deleting contact messages by foundation: {}", foundation.getName());
        contactMessageRepository.deleteByFoundation(foundation);
    }

    @Override
    public void deleteByFoundationId(UUID foundationId) {
        log.debug("Deleting contact messages by foundation ID: {}", foundationId);
        contactMessageRepository.deleteByFoundation_Id(foundationId);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageStatistics getStatistics() {
        log.debug("Getting global contact message statistics");
        
        long totalMessages = contactMessageRepository.count();
        long unreadMessages = contactMessageRepository.countByIsReadFalse();
        long readMessages = contactMessageRepository.countByIsReadTrue();
        
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);
        
        long todayMessages = contactMessageRepository.countByCreatedAtAfter(today);
        long weekMessages = contactMessageRepository.countByCreatedAtAfter(weekAgo);
        long monthMessages = contactMessageRepository.countByCreatedAtAfter(monthAgo);
        
        return new ContactMessageStatistics(totalMessages, unreadMessages, readMessages, 
                                          todayMessages, weekMessages, monthMessages);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageStatistics getStatisticsByFoundation(UUID foundationId) {
        log.debug("Getting contact message statistics for foundation ID: {}", foundationId);
        
        long totalMessages = contactMessageRepository.countByFoundation_Id(foundationId);
        long unreadMessages = contactMessageRepository.countByFoundation_IdAndIsReadFalse(foundationId);
        long readMessages = contactMessageRepository.countByFoundation_IdAndIsReadTrue(foundationId);
        
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime monthAgo = LocalDateTime.now().minusDays(30);
        
        long todayMessages = contactMessageRepository.countByFoundation_IdAndCreatedAtAfter(foundationId, today);
        long weekMessages = contactMessageRepository.countByFoundation_IdAndCreatedAtAfter(foundationId, weekAgo);
        long monthMessages = contactMessageRepository.countByFoundation_IdAndCreatedAtAfter(foundationId, monthAgo);
        
        return new ContactMessageStatistics(totalMessages, unreadMessages, readMessages, 
                                          todayMessages, weekMessages, monthMessages);
    }
}
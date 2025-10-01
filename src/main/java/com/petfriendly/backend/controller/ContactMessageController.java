package com.petfriendly.backend.controller;

import com.petfriendly.backend.entity.ContactMessage;
import com.petfriendly.backend.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for ContactMessage entity operations
 */
@RestController
@RequestMapping("/api/v1/contact-messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private static final Logger log = LoggerFactory.getLogger(ContactMessageController.class);
    private final ContactMessageService contactMessageService;

    /**
     * Create a new contact message
     * POST /api/v1/contact-messages
     */
    @PostMapping
    public ResponseEntity<ContactMessage> createContactMessage(@Valid @RequestBody ContactMessage contactMessage) {
        log.info("Creating new contact message from: {} to foundation ID: {}", 
                contactMessage.getSenderEmail(), contactMessage.getFoundation().getId());
        ContactMessage createdMessage = contactMessageService.createContactMessage(contactMessage);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    /**
     * Get all contact messages
     * GET /api/v1/contact-messages
     */
    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllContactMessages() {
        log.info("Getting all contact messages");
        List<ContactMessage> messages = contactMessageService.findAll();
        return ResponseEntity.ok(messages);
    }

    /**
     * Get all contact messages with pagination
     * GET /api/v1/contact-messages/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<ContactMessage>> getAllContactMessagesWithPagination(Pageable pageable) {
        log.info("Getting all contact messages with pagination");
        Page<ContactMessage> messages = contactMessageService.findAll(pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact message by ID
     * GET /api/v1/contact-messages/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessage> getContactMessageById(@PathVariable UUID id) {
        log.info("Getting contact message by ID: {}", id);
        return contactMessageService.findById(id)
                .map(message -> ResponseEntity.ok(message))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get contact messages by foundation ID
     * GET /api/v1/contact-messages/foundation/{foundationId}
     */
    @GetMapping("/foundation/{foundationId}")
    public ResponseEntity<List<ContactMessage>> getContactMessagesByFoundationId(@PathVariable UUID foundationId) {
        log.info("Getting contact messages by foundation ID: {}", foundationId);
        List<ContactMessage> messages = contactMessageService.findByFoundationId(foundationId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by foundation ID with pagination
     * GET /api/v1/contact-messages/foundation/{foundationId}/page
     */
    @GetMapping("/foundation/{foundationId}/page")
    public ResponseEntity<Page<ContactMessage>> getContactMessagesByFoundationIdWithPagination(@PathVariable UUID foundationId, Pageable pageable) {
        log.info("Getting contact messages by foundation ID with pagination: {}", foundationId);
        Page<ContactMessage> messages = contactMessageService.findByFoundationId(foundationId, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by email
     * GET /api/v1/contact-messages/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<List<ContactMessage>> getContactMessagesByEmail(@PathVariable String email) {
        log.info("Getting contact messages by email: {}", email);
        List<ContactMessage> messages = contactMessageService.findByEmail(email);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by email with pagination
     * GET /api/v1/contact-messages/email/{email}/page
     */
    @GetMapping("/email/{email}/page")
    public ResponseEntity<Page<ContactMessage>> getContactMessagesByEmailWithPagination(@PathVariable String email, Pageable pageable) {
        log.info("Getting contact messages by email with pagination: {}", email);
        Page<ContactMessage> messages = contactMessageService.findByEmail(email, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by name pattern
     * GET /api/v1/contact-messages/search/name?name={namePattern}
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<ContactMessage>> searchContactMessagesByName(@RequestParam String name) {
        log.info("Searching contact messages by name pattern: {}", name);
        List<ContactMessage> messages = contactMessageService.findByNameContaining(name);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by name pattern with pagination
     * GET /api/v1/contact-messages/search/name/page?name={namePattern}
     */
    @GetMapping("/search/name/page")
    public ResponseEntity<Page<ContactMessage>> searchContactMessagesByNameWithPagination(@RequestParam String name, Pageable pageable) {
        log.info("Searching contact messages by name pattern with pagination: {}", name);
        Page<ContactMessage> messages = contactMessageService.findByNameContaining(name, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by subject pattern
     * GET /api/v1/contact-messages/search/subject?subject={subjectPattern}
     */
    @GetMapping("/search/subject")
    public ResponseEntity<List<ContactMessage>> searchContactMessagesBySubject(@RequestParam String subject) {
        log.info("Searching contact messages by subject pattern: {}", subject);
        List<ContactMessage> messages = contactMessageService.findBySubjectContaining(subject);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by subject pattern with pagination
     * GET /api/v1/contact-messages/search/subject/page?subject={subjectPattern}
     */
    @GetMapping("/search/subject/page")
    public ResponseEntity<Page<ContactMessage>> searchContactMessagesBySubjectWithPagination(@RequestParam String subject, Pageable pageable) {
        log.info("Searching contact messages by subject pattern with pagination: {}", subject);
        Page<ContactMessage> messages = contactMessageService.findBySubjectContaining(subject, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by message content pattern
     * GET /api/v1/contact-messages/search/message?message={messagePattern}
     */
    @GetMapping("/search/message")
    public ResponseEntity<List<ContactMessage>> searchContactMessagesByMessage(@RequestParam String message) {
        log.info("Searching contact messages by message pattern: {}", message);
        List<ContactMessage> messages = contactMessageService.findByMessageContaining(message);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get contact messages by message content pattern with pagination
     * GET /api/v1/contact-messages/search/message/page?message={messagePattern}
     */
    @GetMapping("/search/message/page")
    public ResponseEntity<Page<ContactMessage>> searchContactMessagesByMessageWithPagination(@RequestParam String message, Pageable pageable) {
        log.info("Searching contact messages by message pattern with pagination: {}", message);
        Page<ContactMessage> messages = contactMessageService.findByMessageContaining(message, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get read contact messages
     * GET /api/v1/contact-messages/read
     */
    @GetMapping("/read")
    public ResponseEntity<List<ContactMessage>> getReadContactMessages() {
        log.info("Getting read contact messages");
        List<ContactMessage> messages = contactMessageService.findByIsRead(true);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get read contact messages with pagination
     * GET /api/v1/contact-messages/read/page
     */
    @GetMapping("/read/page")
    public ResponseEntity<Page<ContactMessage>> getReadContactMessagesWithPagination(Pageable pageable) {
        log.info("Getting read contact messages with pagination");
        Page<ContactMessage> messages = contactMessageService.findByIsRead(true, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get unread contact messages
     * GET /api/v1/contact-messages/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<List<ContactMessage>> getUnreadContactMessages() {
        log.info("Getting unread contact messages");
        List<ContactMessage> messages = contactMessageService.findByIsRead(false);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get unread contact messages with pagination
     * GET /api/v1/contact-messages/unread/page
     */
    @GetMapping("/unread/page")
    public ResponseEntity<Page<ContactMessage>> getUnreadContactMessagesWithPagination(Pageable pageable) {
        log.info("Getting unread contact messages with pagination");
        Page<ContactMessage> messages = contactMessageService.findByIsRead(false, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get read contact messages by foundation
     * GET /api/v1/contact-messages/foundation/{foundationId}/read
     */
    @GetMapping("/foundation/{foundationId}/read")
    public ResponseEntity<List<ContactMessage>> getReadContactMessagesByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting read contact messages by foundation: {}", foundationId);
        List<ContactMessage> messages = contactMessageService.findByFoundationIdAndIsRead(foundationId, true);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get read contact messages by foundation with pagination
     * GET /api/v1/contact-messages/foundation/{foundationId}/read/page
     */
    @GetMapping("/foundation/{foundationId}/read/page")
    public ResponseEntity<Page<ContactMessage>> getReadContactMessagesByFoundationWithPagination(@PathVariable UUID foundationId, Pageable pageable) {
        log.info("Getting read contact messages by foundation with pagination: {}", foundationId);
        Page<ContactMessage> messages = contactMessageService.findByFoundationIdAndIsRead(foundationId, true, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get unread contact messages by foundation
     * GET /api/v1/contact-messages/foundation/{foundationId}/unread
     */
    @GetMapping("/foundation/{foundationId}/unread")
    public ResponseEntity<List<ContactMessage>> getUnreadContactMessagesByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting unread contact messages by foundation: {}", foundationId);
        List<ContactMessage> messages = contactMessageService.findByFoundationIdAndIsRead(foundationId, false);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get unread contact messages by foundation with pagination
     * GET /api/v1/contact-messages/foundation/{foundationId}/unread/page
     */
    @GetMapping("/foundation/{foundationId}/unread/page")
    public ResponseEntity<Page<ContactMessage>> getUnreadContactMessagesByFoundationWithPagination(@PathVariable UUID foundationId, Pageable pageable) {
        log.info("Getting unread contact messages by foundation with pagination: {}", foundationId);
        Page<ContactMessage> messages = contactMessageService.findByFoundationIdAndIsRead(foundationId, false, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Update contact message
     * PUT /api/v1/contact-messages/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactMessage> updateContactMessage(@PathVariable UUID id, @Valid @RequestBody ContactMessage contactMessage) {
        log.info("Updating contact message with ID: {}", id);
        try {
            ContactMessage updatedMessage = contactMessageService.updateContactMessage(id, contactMessage);
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            log.error("Error updating contact message: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mark contact message as read
     * PUT /api/v1/contact-messages/{id}/mark-read
     */
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<ContactMessage> markContactMessageAsRead(@PathVariable UUID id) {
        log.info("Marking contact message as read: {}", id);
        try {
            ContactMessage readMessage = contactMessageService.markAsRead(id);
            return ResponseEntity.ok(readMessage);
        } catch (RuntimeException e) {
            log.error("Error marking contact message as read: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mark contact message as unread
     * PUT /api/v1/contact-messages/{id}/mark-unread
     */
    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<ContactMessage> markContactMessageAsUnread(@PathVariable UUID id) {
        log.info("Marking contact message as unread: {}", id);
        try {
            ContactMessage unreadMessage = contactMessageService.markAsUnread(id);
            return ResponseEntity.ok(unreadMessage);
        } catch (RuntimeException e) {
            log.error("Error marking contact message as unread: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete contact message
     * DELETE /api/v1/contact-messages/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactMessage(@PathVariable UUID id) {
        log.info("Deleting contact message with ID: {}", id);
        try {
            contactMessageService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting contact message: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if contact message exists by ID
     * HEAD /api/v1/contact-messages/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkContactMessageExists(@PathVariable UUID id) {
        log.info("Checking if contact message exists with ID: {}", id);
        if (contactMessageService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get contact message count
     * GET /api/v1/contact-messages/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getContactMessageCount() {
        log.info("Getting total contact message count");
        long count = contactMessageService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Get contact message count by foundation
     * GET /api/v1/contact-messages/count/foundation/{foundationId}
     */
    @GetMapping("/count/foundation/{foundationId}")
    public ResponseEntity<Long> getContactMessageCountByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting contact message count by foundation: {}", foundationId);
        long count = contactMessageService.countByFoundationId(foundationId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get read contact message count
     * GET /api/v1/contact-messages/count/read
     */
    @GetMapping("/count/read")
    public ResponseEntity<Long> getReadContactMessageCount() {
        log.info("Getting read contact message count");
        long count = contactMessageService.countByIsRead(true);
        return ResponseEntity.ok(count);
    }

    /**
     * Get unread contact message count
     * GET /api/v1/contact-messages/count/unread
     */
    @GetMapping("/count/unread")
    public ResponseEntity<Long> getUnreadContactMessageCount() {
        log.info("Getting unread contact message count");
        long count = contactMessageService.countByIsRead(false);
        return ResponseEntity.ok(count);
    }

    /**
     * Get read contact message count by foundation
     * GET /api/v1/contact-messages/count/foundation/{foundationId}/read
     */
    @GetMapping("/count/foundation/{foundationId}/read")
    public ResponseEntity<Long> getReadContactMessageCountByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting read contact message count by foundation: {}", foundationId);
        long count = contactMessageService.countByFoundationIdAndIsRead(foundationId, true);
        return ResponseEntity.ok(count);
    }

    /**
     * Get unread contact message count by foundation
     * GET /api/v1/contact-messages/count/foundation/{foundationId}/unread
     */
    @GetMapping("/count/foundation/{foundationId}/unread")
    public ResponseEntity<Long> getUnreadContactMessageCountByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting unread contact message count by foundation: {}", foundationId);
        long count = contactMessageService.countByFoundationIdAndIsRead(foundationId, false);
        return ResponseEntity.ok(count);
    }

    /**
     * Get contact message statistics
     * GET /api/v1/contact-messages/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ContactMessageService.ContactMessageStatistics> getContactMessageStatistics() {
        log.info("Getting contact message statistics");
        ContactMessageService.ContactMessageStatistics statistics = contactMessageService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get contact message statistics by foundation
     * GET /api/v1/contact-messages/statistics/foundation/{foundationId}
     */
    @GetMapping("/statistics/foundation/{foundationId}")
    public ResponseEntity<ContactMessageService.ContactMessageStatistics> getContactMessageStatisticsByFoundation(@PathVariable UUID foundationId) {
        log.info("Getting contact message statistics by foundation: {}", foundationId);
        ContactMessageService.ContactMessageStatistics statistics = contactMessageService.getStatisticsByFoundation(foundationId);
        return ResponseEntity.ok(statistics);
    }
}
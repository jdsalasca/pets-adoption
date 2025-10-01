package com.petfriendly.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Contact message response payload")
public record ContactMessageResponse(
        UUID id,
        UUID foundationId,
        String senderName,
        String senderEmail,
        String subject,
        String message,
        boolean isRead,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {}

package com.petfriendly.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Message Response DTO
 */
@Schema(description = "Generic message response")
public class MessageResponse {

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    // Manual constructors since Lombok is not working properly
    public MessageResponse() {}
    
    public MessageResponse(String message) {
        this.message = message;
    }

    // Manual getter and setter
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

}

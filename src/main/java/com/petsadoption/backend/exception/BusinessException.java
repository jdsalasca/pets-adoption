package com.petsadoption.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepción para errores de lógica de negocio.
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final HttpStatus status;
    
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
    
    // Manual getter for compatibility
    public HttpStatus getStatus() {
        return this.status;
    }
}
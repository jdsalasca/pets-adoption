package com.petsadoption.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * DTO genérico para respuestas de API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String error,
        LocalDateTime timestamp
) {
    
    /**
     * Crea una respuesta exitosa con datos.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                true,
                "Operación exitosa",
                data,
                null,
                LocalDateTime.now()
        );
    }
    
    /**
     * Crea una respuesta exitosa con mensaje personalizado.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now()
        );
    }
    
    /**
     * Crea una respuesta exitosa sin datos.
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(
                true,
                message,
                null,
                null,
                LocalDateTime.now()
        );
    }
    
    /**
     * Crea una respuesta de error.
     */
    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(
                false,
                null,
                null,
                error,
                LocalDateTime.now()
        );
    }
    
    /**
     * Crea una respuesta de error con mensaje personalizado.
     */
    public static <T> ApiResponse<T> error(String message, String error) {
        return new ApiResponse<>(
                false,
                message,
                null,
                error,
                LocalDateTime.now()
        );
    }
}
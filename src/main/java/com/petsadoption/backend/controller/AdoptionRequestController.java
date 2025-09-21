package com.petsadoption.backend.controller;

import com.petsadoption.backend.dto.request.AdoptionRequestCreateRequest;
import com.petsadoption.backend.dto.request.AdoptionRequestStatusUpdateRequest;
import com.petsadoption.backend.dto.response.AdoptionRequestResponse;
import com.petsadoption.backend.dto.response.ApiResponse;
import com.petsadoption.backend.service.AdoptionRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/adoptions")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de adopción")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;

    @Operation(summary = "Crear solicitud de adopción",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<AdoptionRequestResponse>> create(
            Authentication authentication,
            @Valid @RequestBody AdoptionRequestCreateRequest request) {
        UUID userId = UUID.fromString(authentication.getName());
        AdoptionRequestResponse response = adoptionRequestService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Solicitud enviada", response));
    }

    @Operation(summary = "Mis solicitudes",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<Page<AdoptionRequestResponse>>> mine(
            Authentication authentication,
            Pageable pageable) {
        UUID userId = UUID.fromString(authentication.getName());
        Page<AdoptionRequestResponse> page = adoptionRequestService.findMine(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Solicitudes encontradas", page));
    }

    @Operation(summary = "Solicitudes por fundación",
            description = "Listado de solicitudes recibidas por una fundación",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/by-foundation")
    public ResponseEntity<ApiResponse<Page<AdoptionRequestResponse>>> byFoundation(
            @RequestParam UUID foundationId,
            Pageable pageable) {
        Page<AdoptionRequestResponse> page = adoptionRequestService.findByFoundation(foundationId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Solicitudes encontradas", page));
    }

    @Operation(summary = "Detalle de solicitud",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdoptionRequestResponse>> get(
            Authentication authentication,
            @PathVariable UUID id) {
        UUID userId = UUID.fromString(authentication.getName());
        AdoptionRequestResponse response = adoptionRequestService.getForUser(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Solicitud encontrada", response));
    }

    @Operation(summary = "Cancelar solicitud",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancel(
            Authentication authentication,
            @PathVariable UUID id) {
        UUID userId = UUID.fromString(authentication.getName());
        adoptionRequestService.cancel(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Solicitud cancelada"));
    }

    @Operation(summary = "Actualizar estado",
            description = "Actualiza el estado de una solicitud (uso de fundaciones)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AdoptionRequestResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam UUID foundationId,
            @Valid @RequestBody AdoptionRequestStatusUpdateRequest request) {
        AdoptionRequestResponse response = adoptionRequestService.updateStatus(id, foundationId, request);
        return ResponseEntity.ok(ApiResponse.success("Solicitud actualizada", response));
    }
}

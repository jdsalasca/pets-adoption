package com.petsadoption.backend.controller;

import com.petsadoption.backend.dto.request.CreatePetRequest;
import com.petsadoption.backend.dto.request.UpdatePetRequest;
import com.petsadoption.backend.dto.response.ApiResponse;
import com.petsadoption.backend.dto.response.PetResponse;
import com.petsadoption.backend.entity.Pet;
import com.petsadoption.backend.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "API para la gestión de mascotas")
public class PetController {

    private final PetService petService;

    @Operation(summary = "Listar mascotas",
            description = "Retorna mascotas con filtros opcionales por ciudad, fundación, especie y estado")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PetResponse>>> listPets(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) UUID foundationId,
            @RequestParam(required = false) Pet.Species species,
            @RequestParam(required = false) Pet.Status status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<PetResponse> pets = petService.findPets(city, foundationId, species, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Mascotas recuperadas", pets));
    }

    @Operation(summary = "Mascotas por fundación",
            description = "Lista las mascotas de una fundación específica")
    @GetMapping("/foundation/{foundationId}")
    public ResponseEntity<ApiResponse<Page<PetResponse>>> listByFoundation(
            @PathVariable UUID foundationId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<PetResponse> pets = petService.getFoundationPets(foundationId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Mascotas recuperadas", pets));
    }

    @Operation(summary = "Obtener mascota",
            description = "Retorna el detalle de una mascota")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PetResponse>> getPet(@PathVariable UUID id) {
        PetResponse pet = petService.getPet(id);
        return ResponseEntity.ok(ApiResponse.success("Mascota encontrada", pet));
    }

    @Operation(summary = "Crear mascota",
            description = "Registra una nueva mascota para una fundación",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    @PreAuthorize("hasRole('FOUNDATION_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PetResponse>> createPet(@Valid @RequestBody CreatePetRequest request) {
        PetResponse created = petService.createPet(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mascota creada", created));
    }

    @Operation(summary = "Actualizar mascota",
            description = "Modifica los datos de una mascota",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FOUNDATION_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PetResponse>> updatePet(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePetRequest request) {
        PetResponse updated = petService.updatePet(id, request);
        return ResponseEntity.ok(ApiResponse.success("Mascota actualizada", updated));
    }

    @Operation(summary = "Actualizar estado",
            description = "Permite cambiar el estado de adopción de una mascota",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('FOUNDATION_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PetResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam @Parameter(description = "Nuevo estado") Pet.Status status) {
        PetResponse updated = petService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Estado actualizado", updated));
    }

    @Operation(summary = "Eliminar mascota",
            description = "Elimina una mascota de la fundación",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FOUNDATION_ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePet(@PathVariable UUID id) {
        petService.deletePet(id);
        return ResponseEntity.ok(ApiResponse.success("Mascota eliminada"));
    }
}

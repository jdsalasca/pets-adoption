package com.petfriendly.backend.controller;

import com.petfriendly.backend.dto.request.AdoptionRequestCreateRequest;
import com.petfriendly.backend.dto.response.MessageResponse;
import com.petfriendly.backend.entity.AdoptionRequest;
import com.petfriendly.backend.entity.AdoptionRequestStatus;
import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.service.AdoptionRequestService;
import com.petfriendly.backend.service.PetService;
import com.petfriendly.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST Controller for AdoptionRequest entity operations
 */
@RestController
@RequestMapping("/api/v1/adoption-requests")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class AdoptionRequestController {

    private final AdoptionRequestService adoptionRequestService;
    private final UserService userService;
    private final PetService petService;

    /**
     * Create a new adoption request
     * POST /api/v1/adoption-requests
     */
    @PostMapping
    @Operation(
            summary = "Submit adoption request",
            description = "Creates a new adoption request for the authenticated user and the provided pet identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Adoption request created",
                    content = @Content(schema = @Schema(implementation = AdoptionRequest.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<AdoptionRequest> createAdoptionRequest(@Valid @RequestBody AdoptionRequestCreateRequest request,
                                                                 @AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = userService.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Pet pet = petService.findById(request.getPetId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

        log.info("Creating new adoption request for pet ID: {} by user ID: {}", pet.getId(), user.getId());

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .user(user)
                .pet(pet)
                .message(request.getMessage())
                .experience(request.getExperience())
                .livingSituation(request.getLivingSituation())
                .build();

        AdoptionRequest createdRequest = adoptionRequestService.createAdoptionRequest(adoptionRequest);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    /**
     * Get all adoption requests
     * GET /api/v1/adoption-requests
     */
    @GetMapping
    public ResponseEntity<List<AdoptionRequest>> getAllAdoptionRequests() {
        log.info("Getting all adoption requests");
        List<AdoptionRequest> requests = adoptionRequestService.findAll();
        return ResponseEntity.ok(requests);
    }

    /**
     * Get all adoption requests with pagination
     * GET /api/v1/adoption-requests/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<AdoptionRequest>> getAllAdoptionRequestsWithPagination(Pageable pageable) {
        log.info("Getting all adoption requests with pagination");
        Page<AdoptionRequest> requests = adoptionRequestService.findAll(pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption request by ID
     * GET /api/v1/adoption-requests/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdoptionRequest> getAdoptionRequestById(@PathVariable UUID id) {
        log.info("Getting adoption request by ID: {}", id);
        return adoptionRequestService.findById(id)
                .map(request -> ResponseEntity.ok(request))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get adoption requests by user ID
     * GET /api/v1/adoption-requests/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByUserId(@PathVariable UUID userId) {
        log.info("Getting adoption requests by user ID: {}", userId);
        List<AdoptionRequest> requests = adoptionRequestService.findByUserId(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by user ID with pagination
     * GET /api/v1/adoption-requests/user/{userId}/page
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<AdoptionRequest>> getAdoptionRequestsByUserIdWithPagination(@PathVariable UUID userId, Pageable pageable) {
        log.info("Getting adoption requests by user ID with pagination: {}", userId);
        Page<AdoptionRequest> requests = adoptionRequestService.findByUserId(userId, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by pet ID
     * GET /api/v1/adoption-requests/pet/{petId}
     */
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByPetId(@PathVariable UUID petId) {
        log.info("Getting adoption requests by pet ID: {}", petId);
        List<AdoptionRequest> requests = adoptionRequestService.findByPetId(petId);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by pet ID with pagination
     * GET /api/v1/adoption-requests/pet/{petId}/page
     */
    @GetMapping("/pet/{petId}/page")
    public ResponseEntity<Page<AdoptionRequest>> getAdoptionRequestsByPetIdWithPagination(@PathVariable UUID petId, Pageable pageable) {
        log.info("Getting adoption requests by pet ID with pagination: {}", petId);
        Page<AdoptionRequest> requests = adoptionRequestService.findByPetId(petId, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by status
     * GET /api/v1/adoption-requests/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByStatus(@PathVariable AdoptionRequestStatus status) {
        log.info("Getting adoption requests by status: {}", status);
        List<AdoptionRequest> requests = adoptionRequestService.findByStatus(status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by status with pagination
     * GET /api/v1/adoption-requests/status/{status}/page
     */
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<AdoptionRequest>> getAdoptionRequestsByStatusWithPagination(@PathVariable AdoptionRequestStatus status, Pageable pageable) {
        log.info("Getting adoption requests by status with pagination: {}", status);
        Page<AdoptionRequest> requests = adoptionRequestService.findByStatus(status, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by user and status
     * GET /api/v1/adoption-requests/user/{userId}/status/{status}
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByUserAndStatus(@PathVariable UUID userId, @PathVariable AdoptionRequestStatus status) {
        log.info("Getting adoption requests by user ID: {} and status: {}", userId, status);
        List<AdoptionRequest> requests = adoptionRequestService.findByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by user and status with pagination
     * GET /api/v1/adoption-requests/user/{userId}/status/{status}/page
     */
    @GetMapping("/user/{userId}/status/{status}/page")
    public ResponseEntity<Page<AdoptionRequest>> getAdoptionRequestsByUserAndStatusWithPagination(@PathVariable UUID userId, @PathVariable AdoptionRequestStatus status, Pageable pageable) {
        log.info("Getting adoption requests by user ID: {} and status: {} with pagination", userId, status);
        Page<AdoptionRequest> requests = adoptionRequestService.findByUserIdAndStatus(userId, status, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by pet and status
     * GET /api/v1/adoption-requests/pet/{petId}/status/{status}
     */
    @GetMapping("/pet/{petId}/status/{status}")
    public ResponseEntity<List<AdoptionRequest>> getAdoptionRequestsByPetAndStatus(@PathVariable UUID petId, @PathVariable AdoptionRequestStatus status) {
        log.info("Getting adoption requests by pet ID: {} and status: {}", petId, status);
        List<AdoptionRequest> requests = adoptionRequestService.findByPetIdAndStatus(petId, status);
        return ResponseEntity.ok(requests);
    }

    /**
     * Get adoption requests by pet and status with pagination
     * GET /api/v1/adoption-requests/pet/{petId}/status/{status}/page
     */
    @GetMapping("/pet/{petId}/status/{status}/page")
    public ResponseEntity<Page<AdoptionRequest>> getAdoptionRequestsByPetAndStatusWithPagination(@PathVariable UUID petId, @PathVariable AdoptionRequestStatus status, Pageable pageable) {
        log.info("Getting adoption requests by pet ID: {} and status: {} with pagination", petId, status);
        Page<AdoptionRequest> requests = adoptionRequestService.findByPetIdAndStatus(petId, status, pageable);
        return ResponseEntity.ok(requests);
    }

    /**
     * Update adoption request
     * PUT /api/v1/adoption-requests/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdoptionRequest> updateAdoptionRequest(@PathVariable UUID id, @Valid @RequestBody AdoptionRequest adoptionRequest) {
        log.info("Updating adoption request with ID: {}", id);
        try {
            AdoptionRequest updatedRequest = adoptionRequestService.updateAdoptionRequest(id, adoptionRequest);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            log.error("Error updating adoption request: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update adoption request status
     * PUT /api/v1/adoption-requests/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AdoptionRequest> updateAdoptionRequestStatus(@PathVariable UUID id, @RequestParam AdoptionRequestStatus status) {
        log.info("Updating adoption request status with ID: {} to status: {}", id, status);
        try {
            AdoptionRequest updatedRequest = adoptionRequestService.updateStatus(id, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            log.error("Error updating adoption request status: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Approve adoption request
     * PUT /api/v1/adoption-requests/{id}/approve
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<AdoptionRequest> approveAdoptionRequest(@PathVariable UUID id) {
        log.info("Approving adoption request with ID: {}", id);
        try {
            AdoptionRequest approvedRequest = adoptionRequestService.approveRequest(id);
            return ResponseEntity.ok(approvedRequest);
        } catch (RuntimeException e) {
            log.error("Error approving adoption request: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Reject adoption request
     * PUT /api/v1/adoption-requests/{id}/reject
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<AdoptionRequest> rejectAdoptionRequest(@PathVariable UUID id) {
        log.info("Rejecting adoption request with ID: {}", id);
        try {
            AdoptionRequest rejectedRequest = adoptionRequestService.rejectRequest(id);
            return ResponseEntity.ok(rejectedRequest);
        } catch (RuntimeException e) {
            log.error("Error rejecting adoption request: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cancel adoption request
     * PUT /api/v1/adoption-requests/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AdoptionRequest> cancelAdoptionRequest(@PathVariable UUID id) {
        log.info("Canceling adoption request with ID: {}", id);
        try {
            AdoptionRequest canceledRequest = adoptionRequestService.cancelRequest(id);
            return ResponseEntity.ok(canceledRequest);
        } catch (RuntimeException e) {
            log.error("Error canceling adoption request: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete adoption request
     * DELETE /api/v1/adoption-requests/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoptionRequest(@PathVariable UUID id) {
        log.info("Deleting adoption request with ID: {}", id);
        try {
            adoptionRequestService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting adoption request: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if adoption request exists by ID
     * HEAD /api/v1/adoption-requests/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkAdoptionRequestExists(@PathVariable UUID id) {
        log.info("Checking if adoption request exists with ID: {}", id);
        if (adoptionRequestService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if user has requested adoption for pet
     * HEAD /api/v1/adoption-requests/user/{userId}/pet/{petId}
     */
    @RequestMapping(value = "/user/{userId}/pet/{petId}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkUserHasRequestedAdoption(@PathVariable UUID userId, @PathVariable UUID petId) {
        log.info("Checking if user {} has requested adoption for pet {}", userId, petId);
        if (adoptionRequestService.hasUserRequestedAdoption(userId, petId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get adoption request count
     * GET /api/v1/adoption-requests/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getAdoptionRequestCount() {
        log.info("Getting total adoption request count");
        long count = adoptionRequestService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Get adoption request count by status
     * GET /api/v1/adoption-requests/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getAdoptionRequestCountByStatus(@PathVariable AdoptionRequestStatus status) {
        log.info("Getting adoption request count by status: {}", status);
        long count = adoptionRequestService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    /**
     * Get adoption request count by user
     * GET /api/v1/adoption-requests/count/user/{userId}
     */
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> getAdoptionRequestCountByUser(@PathVariable UUID userId) {
        log.info("Getting adoption request count by user: {}", userId);
        long count = adoptionRequestService.countByUserId(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get adoption request count by pet
     * GET /api/v1/adoption-requests/count/pet/{petId}
     */
    @GetMapping("/count/pet/{petId}")
    public ResponseEntity<Long> getAdoptionRequestCountByPet(@PathVariable UUID petId) {
        log.info("Getting adoption request count by pet: {}", petId);
        long count = adoptionRequestService.countByPetId(petId);
        return ResponseEntity.ok(count);
    }

    /**
     * Get adoption request statistics
     * GET /api/v1/adoption-requests/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<AdoptionRequestService.AdoptionRequestStatistics> getAdoptionRequestStatistics() {
        log.info("Getting adoption request statistics");
        AdoptionRequestService.AdoptionRequestStatistics statistics = adoptionRequestService.getStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get adoption request statistics by user
     * GET /api/v1/adoption-requests/statistics/user/{userId}
     */
    @GetMapping("/statistics/user/{userId}")
    public ResponseEntity<AdoptionRequestService.AdoptionRequestStatistics> getAdoptionRequestStatisticsByUser(@PathVariable UUID userId) {
        log.info("Getting adoption request statistics by user: {}", userId);
        AdoptionRequestService.AdoptionRequestStatistics statistics = adoptionRequestService.getStatisticsByUser(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get adoption request statistics by pet
     * GET /api/v1/adoption-requests/statistics/pet/{petId}
     */
    @GetMapping("/statistics/pet/{petId}")
    public ResponseEntity<AdoptionRequestService.AdoptionRequestStatistics> getAdoptionRequestStatisticsByPet(@PathVariable UUID petId) {
        log.info("Getting adoption request statistics by pet: {}", petId);
        AdoptionRequestService.AdoptionRequestStatistics statistics = adoptionRequestService.getStatisticsByPet(petId);
        return ResponseEntity.ok(statistics);
    }
}

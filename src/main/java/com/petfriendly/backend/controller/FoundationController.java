package com.petfriendly.backend.controller;

import com.petfriendly.backend.entity.Foundation;
import com.petfriendly.backend.service.FoundationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Foundation entity operations
 */
@RestController
@RequestMapping("/api/v1/foundations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FoundationController {

    private final FoundationService foundationService;

    /**
     * Create a new foundation
     * POST /api/v1/foundations
     */
    @PostMapping
    public ResponseEntity<Foundation> createFoundation(@Valid @RequestBody Foundation foundation) {
        log.info("Creating new foundation: {}", foundation.getName());
        Foundation createdFoundation = foundationService.createFoundation(foundation);
        return new ResponseEntity<>(createdFoundation, HttpStatus.CREATED);
    }

    /**
     * Get all foundations
     * GET /api/v1/foundations
     */
    @GetMapping
    public ResponseEntity<List<Foundation>> getAllFoundations() {
        log.info("Getting all foundations");
        List<Foundation> foundations = foundationService.findAll();
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get all foundations with pagination
     * GET /api/v1/foundations/page
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Foundation>> getAllFoundationsWithPagination(Pageable pageable) {
        log.info("Getting all foundations with pagination");
        Page<Foundation> foundations = foundationService.findAll(pageable);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get foundation by ID
     * GET /api/v1/foundations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Foundation> getFoundationById(@PathVariable UUID id) {
        log.info("Getting foundation by ID: {}", id);
        return foundationService.findById(id)
                .map(foundation -> ResponseEntity.ok(foundation))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get foundation by name
     * GET /api/v1/foundations/name/{name}
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Foundation> getFoundationByName(@PathVariable String name) {
        log.info("Getting foundation by name: {}", name);
        return foundationService.findByName(name)
                .map(foundation -> ResponseEntity.ok(foundation))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get foundation by email
     * GET /api/v1/foundations/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Foundation> getFoundationByEmail(@PathVariable String email) {
        log.info("Getting foundation by email: {}", email);
        return foundationService.findByEmail(email)
                .map(foundation -> ResponseEntity.ok(foundation))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get foundations by city
     * GET /api/v1/foundations/city/{city}
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Foundation>> getFoundationsByCity(@PathVariable String city) {
        log.info("Getting foundations by city: {}", city);
        List<Foundation> foundations = foundationService.findByCity(city);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get foundations by city with pagination
     * GET /api/v1/foundations/city/{city}/page
     */
    @GetMapping("/city/{city}/page")
    public ResponseEntity<Page<Foundation>> getFoundationsByCityWithPagination(@PathVariable String city, Pageable pageable) {
        log.info("Getting foundations by city with pagination: {}", city);
        Page<Foundation> foundations = foundationService.findByCity(city, pageable);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get foundations by state
     * GET /api/v1/foundations/state/{state}
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Foundation>> getFoundationsByState(@PathVariable String state) {
        log.info("Getting foundations by state: {}", state);
        List<Foundation> foundations = foundationService.findByState(state);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get foundations by state with pagination
     * GET /api/v1/foundations/state/{state}/page
     */
    @GetMapping("/state/{state}/page")
    public ResponseEntity<Page<Foundation>> getFoundationsByStateWithPagination(@PathVariable String state, Pageable pageable) {
        log.info("Getting foundations by state with pagination: {}", state);
        Page<Foundation> foundations = foundationService.findByState(state, pageable);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get active foundations
     * GET /api/v1/foundations/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<Foundation>> getActiveFoundations() {
        log.info("Getting active foundations");
        List<Foundation> foundations = foundationService.findActiveFoundations();
        return ResponseEntity.ok(foundations);
    }

    /**
     * Get active foundations with pagination
     * GET /api/v1/foundations/active/page
     */
    @GetMapping("/active/page")
    public ResponseEntity<Page<Foundation>> getActiveFoundationsWithPagination(Pageable pageable) {
        log.info("Getting active foundations with pagination");
        Page<Foundation> foundations = foundationService.findActiveFoundations(pageable);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Search foundations by name
     * GET /api/v1/foundations/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Foundation>> searchFoundationsByName(@RequestParam String name) {
        log.info("Searching foundations by name: {}", name);
        List<Foundation> foundations = foundationService.findByNameContaining(name);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Search foundations by name with pagination
     * GET /api/v1/foundations/search/page?name={name}
     */
    @GetMapping("/search/page")
    public ResponseEntity<Page<Foundation>> searchFoundationsByNameWithPagination(@RequestParam String name, Pageable pageable) {
        log.info("Searching foundations by name with pagination: {}", name);
        Page<Foundation> foundations = foundationService.findByNameContaining(name, pageable);
        return ResponseEntity.ok(foundations);
    }

    /**
     * Update foundation
     * PUT /api/v1/foundations/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Foundation> updateFoundation(@PathVariable UUID id, @Valid @RequestBody Foundation foundation) {
        log.info("Updating foundation with ID: {}", id);
        try {
            Foundation updatedFoundation = foundationService.updateFoundation(id, foundation);
            return ResponseEntity.ok(updatedFoundation);
        } catch (RuntimeException e) {
            log.error("Error updating foundation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activate foundation
     * PUT /api/v1/foundations/{id}/activate
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<Foundation> activateFoundation(@PathVariable UUID id) {
        log.info("Activating foundation with ID: {}", id);
        try {
            Foundation activatedFoundation = foundationService.activateFoundation(id);
            return ResponseEntity.ok(activatedFoundation);
        } catch (RuntimeException e) {
            log.error("Error activating foundation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deactivate foundation
     * PUT /api/v1/foundations/{id}/deactivate
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Foundation> deactivateFoundation(@PathVariable UUID id) {
        log.info("Deactivating foundation with ID: {}", id);
        try {
            Foundation deactivatedFoundation = foundationService.deactivateFoundation(id);
            return ResponseEntity.ok(deactivatedFoundation);
        } catch (RuntimeException e) {
            log.error("Error deactivating foundation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete foundation
     * DELETE /api/v1/foundations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoundation(@PathVariable UUID id) {
        log.info("Deleting foundation with ID: {}", id);
        try {
            foundationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting foundation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if foundation exists by ID
     * HEAD /api/v1/foundations/{id}
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkFoundationExists(@PathVariable UUID id) {
        log.info("Checking if foundation exists with ID: {}", id);
        if (foundationService.existsById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if foundation name exists
     * HEAD /api/v1/foundations/name/{name}
     */
    @RequestMapping(value = "/name/{name}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkFoundationNameExists(@PathVariable String name) {
        log.info("Checking if foundation name exists: {}", name);
        if (foundationService.existsByName(name)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Check if foundation email exists
     * HEAD /api/v1/foundations/email/{email}
     */
    @RequestMapping(value = "/email/{email}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkFoundationEmailExists(@PathVariable String email) {
        log.info("Checking if foundation email exists: {}", email);
        if (foundationService.existsByEmail(email)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get foundation count
     * GET /api/v1/foundations/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getFoundationCount() {
        log.info("Getting total foundation count");
        long count = foundationService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Get active foundation count
     * GET /api/v1/foundations/count/active
     */
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveFoundationCount() {
        log.info("Getting active foundation count");
        long count = foundationService.countActiveFoundations();
        return ResponseEntity.ok(count);
    }

    /**
     * Get foundation count by city
     * GET /api/v1/foundations/count/city/{city}
     */
    @GetMapping("/count/city/{city}")
    public ResponseEntity<Long> getFoundationCountByCity(@PathVariable String city) {
        log.info("Getting foundation count by city: {}", city);
        long count = foundationService.countByCity(city);
        return ResponseEntity.ok(count);
    }

    /**
     * Get foundation count by state
     * GET /api/v1/foundations/count/state/{state}
     */
    @GetMapping("/count/state/{state}")
    public ResponseEntity<Long> getFoundationCountByState(@PathVariable String state) {
        log.info("Getting foundation count by state: {}", state);
        long count = foundationService.countByState(state);
        return ResponseEntity.ok(count);
    }

    /**
     * Get foundation statistics
     * GET /api/v1/foundations/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<FoundationService.FoundationStatistics> getFoundationStatistics() {
        log.info("Getting foundation statistics");
        FoundationService.FoundationStatistics statistics = foundationService.getStatistics();
        return ResponseEntity.ok(statistics);
    }
}
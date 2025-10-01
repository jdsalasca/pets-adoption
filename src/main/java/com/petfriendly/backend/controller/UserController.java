package com.petfriendly.backend.controller;

import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.enums.Role;
import com.petfriendly.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user. Requires SUPER_ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns all platform users. Requires SUPER_ADMIN role.")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Getting all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<User>> getAllUsersWithPagination(Pageable pageable) {
        log.info("Getting all users with pagination");
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        log.info("Fetching user with ID: {}", id);
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieve a user by their email address")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        log.info("Getting user by email: {}", email);
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        log.info("Getting users by role: {}", role);
        Role requestedRole = Role.valueOf(role.toUpperCase());
        return ResponseEntity.ok(userService.findByRole(requestedRole));
    }

    @GetMapping("/role/{role}/page")
    public ResponseEntity<Page<User>> getUsersByRoleWithPagination(@PathVariable String role, Pageable pageable) {
        log.info("Getting users by role with pagination: {}", role);
        Role requestedRole = Role.valueOf(role.toUpperCase());
        return ResponseEntity.ok(userService.findByRole(requestedRole, pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        log.info("Getting active users");
        return ResponseEntity.ok(userService.findActiveUsers());
    }

    @GetMapping("/active/page")
    public ResponseEntity<Page<User>> getActiveUsers(Pageable pageable) {
        log.info("Getting active users with pagination");
        return ResponseEntity.ok(userService.findActiveUsers(pageable));
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<User>> searchUsersByName(@PathVariable String name) {
        log.info("Searching users by name: {}", name);
        return ResponseEntity.ok(userService.findByNameContaining(name));
    }

    @GetMapping("/search/{name}/page")
    public ResponseEntity<Page<User>> searchUsersByName(@PathVariable String name, Pageable pageable) {
        log.info("Searching users by name with pagination: {}", name);
        return ResponseEntity.ok(userService.findByNameContaining(name, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        log.info("Updating user with ID: {}", id);
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (IllegalArgumentException ex) {
            log.error("Error updating user: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable UUID id) {
        log.info("Activating user with ID: {}", id);
        try {
            return ResponseEntity.ok(userService.activateUser(id));
        } catch (IllegalArgumentException ex) {
            log.error("Error activating user: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable UUID id) {
        log.info("Deactivating user with ID: {}", id);
        try {
            return ResponseEntity.ok(userService.deactivateUser(id));
        } catch (IllegalArgumentException ex) {
            log.error("Error deactivating user: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with ID: {}", id);
        try {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            log.error("Error deleting user: {}", ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkUserExists(@PathVariable UUID id) {
        return userService.existsById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/email/{email}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkEmailExists(@PathVariable String email) {
        return userService.existsByEmail(email) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check if user exists by email", description = "Check if a user with the given email exists")
    @ApiResponse(responseCode = "200", description = "User exists")
    @ApiResponse(responseCode = "404", description = "User does not exist")
    public ResponseEntity<Void> checkUserExistsByEmail(@PathVariable String email) {
        return userService.existsByEmail(email) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.ok(userService.count());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveUserCount() {
        return ResponseEntity.ok(userService.countActiveUsers());
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> getUserCountByRole(@PathVariable String role) {
        Role requestedRole = Role.valueOf(role.toUpperCase());
        return ResponseEntity.ok(userService.countByRole(requestedRole));
    }

    @GetMapping("/statistics")
    public ResponseEntity<UserService.UserStatistics> getUserStatistics() {
        return ResponseEntity.ok(userService.getStatistics());
    }
}

package com.petfriendly.backend.controller;

import com.petfriendly.backend.dto.request.LoginRequest;
import com.petfriendly.backend.dto.request.RegisterRequest;
import com.petfriendly.backend.dto.response.JwtAuthenticationResponse;
import com.petfriendly.backend.dto.response.MessageResponse;
import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.enums.Role;
import com.petfriendly.backend.security.JwtTokenProvider;
import com.petfriendly.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user authentication and registration
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authentication succeeded",
            content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            log.info("User {} logged in successfully", loginRequest.getEmail());
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer"));
        } catch (org.springframework.security.core.AuthenticationException ex) {
            log.warn("Authentication failed for email {}: {}", loginRequest.getEmail(), ex.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid email or password"));
        } catch (Exception ex) {
            log.error("Unexpected error during login for email {}", loginRequest.getEmail(), ex);
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Login failed due to server error: " + ex.getClass().getSimpleName()));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation problem",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "409", description = "Email already taken",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration attempt for email: {}", registerRequest.getEmail());
        
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Email is already taken!"));
        }

        // Create new user account
        // Build user with raw password; service layer will handle encoding
        User user = User.builder()
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .email(registerRequest.getEmail())
            .password(registerRequest.getPassword())
            .phone(registerRequest.getPhone())
            .city(registerRequest.getCity())
            .role(Role.USER)
            .active(true)
            .build();

        userService.createUser(user);
        
        log.info("User {} registered successfully", registerRequest.getEmail());
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/register/foundation")
    @Operation(summary = "Foundation registration", description = "Register a new foundation account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Foundation registered",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation problem",
            content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "409", description = "Email already taken",
            content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    })
    public ResponseEntity<MessageResponse> registerFoundation(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Foundation registration attempt for email: {}", registerRequest.getEmail());
        
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Email is already taken!"));
        }

        // Create new foundation account
        // Build foundation admin with raw password; service layer will handle encoding
        User user = User.builder()
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .email(registerRequest.getEmail())
            .password(registerRequest.getPassword())
            .phone(registerRequest.getPhone())
            .city(registerRequest.getCity())
            .role(Role.FOUNDATION_ADMIN)
            .active(true)
            .build();

        userService.createUser(user);
        
        log.info("Foundation {} registered successfully", registerRequest.getEmail());
        
        return ResponseEntity.ok(new MessageResponse("Foundation registered successfully!"));
    }
}

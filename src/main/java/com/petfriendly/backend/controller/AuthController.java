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
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        
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
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
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
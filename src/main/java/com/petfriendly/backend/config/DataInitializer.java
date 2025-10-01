package com.petfriendly.backend.config;

import com.petfriendly.backend.entity.User;
import com.petfriendly.backend.enums.Role;
import com.petfriendly.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds demo users for Swagger/Postman testing in non-test environments.
 */
@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String DEMO_USER_EMAIL = "demo.user@petfriendly.dev";
    private static final String DEMO_ADMIN_EMAIL = "demo.admin@petfriendly.dev";

    private final UserService userService;

    @Override
    @Transactional
    public void run(String... args) {
        ensureUser(DEMO_USER_EMAIL, "DemoPa55!", Role.USER, "Demo", "User");
        ensureUser(DEMO_ADMIN_EMAIL, "AdminPa55!", Role.SUPER_ADMIN, "Demo", "Admin");
    }

    private void ensureUser(String email, String rawPassword, Role role, String firstName, String lastName) {
        userService.findByEmail(email).ifPresentOrElse(
            existing -> log.debug("Demo account {} already exists", email),
            () -> {
                User demo = User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(rawPassword)
                        .phone("+573000000000")
                        .city("Bogota")
                        .role(role)
                        .active(true)
                        .build();
                userService.createUser(demo);
                log.info("Created demo account: {}", email);
            }
        );
    }
}

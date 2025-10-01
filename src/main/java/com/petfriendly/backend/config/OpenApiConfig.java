package com.petfriendly.backend.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI configuration for Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PetFriendly Adoption API",
                version = "1.0.0",
                description = """
                        REST API for the PetFriendly platform. Demo credentials shipped with the app:
                        - User account: demo.user@petfriendly.dev / DemoPa55!
                        - Admin account: demo.admin@petfriendly.dev / AdminPa55!

                        1. Call /api/v1/auth/login with one of the accounts.
                        2. Click \"Authorize\" in Swagger UI and paste the access token as `Bearer <token>`.

                        Local environment runs on http://localhost:8080. External docs link to the public repository.
                        """,
                contact = @Contact(name = "PetFriendly Team", email = "hello@petfriendly.dev", url = "https://petfriendly.dev"),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development"),
                @Server(url = "https://api.petfriendly.dev", description = "Production")
        },
        security = @SecurityRequirement(name = "bearerAuth"),
        externalDocs = @ExternalDocumentation(description = "GitHub repository", url = "https://github.com/jdsalasca/pets-adoption")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}

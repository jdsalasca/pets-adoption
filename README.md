# PetFriendly Backend

PetFriendly Backend is a Spring Boot service that powers the PetFriendly adoption platform. It exposes RESTful APIs for managing pets, foundations, users, adoption requests, and contact messages, with JWT-based authentication and database migrations via Flyway.

## Features
- User authentication with JWT tokens and role-based authorization.
- Pet catalog management including images, species, and statuses.
- Adoption request lifecycle with rich statistics and filtering.
- Foundation onboarding workflow and contact messaging.
- Database migrations managed with Flyway and PostgreSQL support.
- Developer-friendly H2 in-memory profile for local testing.

## Tech Stack
- Java 21
- Spring Boot 3.4.x (Web, Data JPA, Security, Validation)
- PostgreSQL (production) / H2 (development profile)
- Flyway database migrations
- Maven build system
- Lombok (optional, manual accessors included for compatibility)

## Getting Started

### Prerequisites
- Java 21 (Adoptium Temurin recommended)
- Maven 3.9+
- PostgreSQL instance (for non-dev profiles)

Confirm your versions:

```bash
mvn -v
java -version
```

### Environment Variables
Copy `.env.example` to `.env` (or export variables manually) and populate the values:

- `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION`
- `SPRING_PROFILES_ACTIVE`
- Optional Supabase fields if you deploy there.

For local development you can leave `SPRING_PROFILES_ACTIVE=dev` to leverage the in-memory H2 configuration provided in `src/main/resources/application-dev.yml`.

### Build & Run

```bash
# Clean build without tests
mvn clean package -DskipTests

# Run the application with the dev profile (in-memory H2)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or run the packaged jar
target\petfriendly-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

To run against PostgreSQL, ensure the connection variables are set (environment or `.env`) and omit the profile override so `application.yml` is used.

### Database Migrations
Flyway migrations live in `src/main/resources/db/migration`. On start-up with the default profile, Flyway will validate and apply migrations. The `dev` profile disables Flyway and schema initialization to let Hibernate manage the schema in the in-memory database.

### API Documentation
The project bundles Springdoc OpenAPI. When the application is running you can visit:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Testing

```bash
mvn test
```

Add integration tests under `src/test/java` to validate service and controller logic. The dev profile can be leveraged for fast in-memory tests.

## Troubleshooting
- Ensure Lombok annotation processing is enabled in your IDE if you rely on Lombok-generated methods.
- For `mvn clean package` failures, run with `-X` to get detailed logs.
- Verify environment variables with `mvn help:evaluate -Dexpression=...` if values are not being picked up.

## Contributing
1. Create a feature branch.
2. Run `mvn clean package` before pushing changes.
3. Submit a pull request describing your updates and testing performed.

## License
Provide licensing details here when available.

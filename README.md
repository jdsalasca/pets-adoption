# PetFriendly Backend

PetFriendly Backend is the Spring Boot API behind the PetFriendly adoption platform. It exposes RESTful endpoints for pets, foundations, users, adoption requests, contact messages and reporting, secured with JWT and backed by Flyway-managed PostgreSQL schemas.

![Sample Pet](https://images.unsplash.com/photo-1507146426996-ef05306b995a?auto=format&fit=crop&w=960&q=80)

> Source code: [github.com/jdsalasca/pets-adoption](https://github.com/jdsalasca/pets-adoption)

## Features
- User authentication with JWT tokens and role-based authorization (USER, FOUNDATION_ADMIN, SUPER_ADMIN).
- Pet catalog management including images, species, and statuses.
- Adoption request lifecycle with rich statistics and filtering.
- Foundation onboarding workflow and contact messaging.
- Database migrations managed with Flyway and PostgreSQL support.
- Developer-friendly H2 in-memory profile for local testing.
- Observability via Spring Boot Actuator (health, info, metrics).

### Demo Credentials

Swagger UI and the sample Postman collection rely on a demo account:

| Role | Email | Password |
| --- | --- | --- |
| Standard user | `demo.user@petfriendly.dev` | `DemoPa55!` |

Authenticate once in Swagger (`Authorize` button) and the UI will attach the bearer token to every secured request.

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

Each secured endpoint declares a `bearerAuth` requirement so the UI automatically adds `Authorization: Bearer <token>` after you authenticate.

### Monitoring & Health

Spring Boot Actuator is enabled when the application starts. The most common endpoints are:

| Endpoint | Description |
| --- | --- |
| `GET /actuator/health` | Liveness probe (public) |
| `GET /actuator/info` | Build information (public) |
| `GET /actuator/metrics` | Micrometer metrics (requires SUPER_ADMIN) |

### Testing

```bash
mvn test
```

Add integration tests under `src/test/java` to validate service and controller logic. The dev profile can be leveraged for fast in-memory tests.

Quick smoke test with `curl`:

```bash
# Register demo user (idempotent on dev profile)
curl -s -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"firstName":"Demo","lastName":"User","email":"demo.user@petfriendly.dev","password":"DemoPa55!","phone":"+573001112233","city":"Bogota"}'

# Login and capture token
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"demo.user@petfriendly.dev","password":"DemoPa55!"}' | jq -r '.accessToken')

# Call a protected endpoint
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/adoption-requests/count
```

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

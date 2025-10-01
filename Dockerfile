# ---- Build Stage ----------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Cache Maven dependencies
WORKDIR /build
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

# Copy project sources and build the application
COPY src ./src
COPY .mvn ./.mvn
COPY mvnw* ./
RUN mvn -B -q clean package -DskipTests

# ---- Runtime Stage --------------------------------------------------------
FROM eclipse-temurin:21-jre

# Render expects the app to listen on $PORT (defaults to 8080)
ENV PORT=8080
EXPOSE ${PORT}

# Copy the Spring Boot fat jar from the builder stage
COPY --from=builder /build/target/petfriendly-backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Health check (optional but recommended by Render)
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:${PORT}/actuator/health || exit 1

# Run the application; Render injects env vars at runtime
ENTRYPOINT ["java","-jar","/app/app.jar"]

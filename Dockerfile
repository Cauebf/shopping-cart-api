# Stage 1: Build stage (Maven + JDK 21)
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application and generate the JAR file (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage (final image with JRE 21 only)
# The build stage is discarded after this stage, except for the built JAR file (keeping the final image lightweight)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR file from the build stage, everything else in the build stage is discarded
COPY --from=build /app/target/shopping-cart-api-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Use a base image with Java installed
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY build/libs/ms-wallet-service-0.0.1.jar app.jar

# Expose the application's port (e.g., 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

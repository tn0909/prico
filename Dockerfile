# Use a Maven image with Java 8 installed
FROM maven:3.8.4-openjdk-8-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the source code into the container
COPY . .

# Build the application using Maven
RUN mvn package -DskipTests=true

RUN ls

# Create a new stage for the final image
FROM openjdk:8u212-jdk-stretch

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage into the final image
COPY --from=build /app/target/prico-0.0.1-SNAPSHOT.jar /app/prico.jar

# Expose the port your Spring Boot application listens on
EXPOSE 8080

# Command to run the Spring Boot application
CMD ["java", "-jar", "prico.jar"]
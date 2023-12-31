# Use the official Maven image to create a build artifact
FROM maven:3.8.4-openjdk-17 as builder

# Copy the project files into the image
WORKDIR /app
COPY src /app/src
COPY pom.xml /app

# Package the application
RUN mvn clean package && ls -la /app/target

# Use OpenJDK for the runtime
FROM openjdk:17-slim

# Copy the built jar file from the builder stage
COPY --from=builder /app/target/Number*-with-dependencies.jar /Number.jar

# Run the bot
CMD ["java", "-jar", "/Number.jar"]

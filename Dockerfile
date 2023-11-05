# Use the official Maven image for a build stage
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory
WORKDIR /home/app

# Copy the project files to the container
COPY src /home/app/src
COPY pom.xml /home/app

# Package the application
RUN mvn clean package

# Use the official OpenJDK image for a slim runtime stage
FROM openjdk:17-alpine

# Copy the built jar file from the build stage
COPY --from=build /home/app/target/*.jar /usr/local/lib/application.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/usr/local/lib/application.jar"]

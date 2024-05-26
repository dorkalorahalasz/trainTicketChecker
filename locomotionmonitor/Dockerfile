# Use the official Maven image with Maven 3.8.5 and OpenJDK 17 to build the project
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and the source code into the container
COPY pom.xml .
COPY src ./src

# Package the application (execute Maven package command)
RUN mvn clean package

# Use the official OpenJDK runtime image to run the application
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file from the build stage
#COPY --from=build /app/target/*-shaded.jar app.jar
COPY --from=build /app/target/locomotionmonitor-1.0-SNAPSHOT-shaded.jar app.jar


# Define the command to run the application
CMD ["java", "-jar", "app.jar"]

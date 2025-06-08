# Use an official OpenJDK image as the base image
FROM openjdk:25-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy your actual JAR file into the container with its original name
COPY target/eKart-0.0.1-SNAPSHOT.jar /app/eKart-0.0.1-SNAPSHOT.jar

# Create the logs directory and set permissions
RUN mkdir -p /app/logs && chmod -R 777 /app/logs

# Create the upload directory for images inside the container
RUN mkdir -p /app/static/images && chmod -R 777 /app/static/images

# Expose the port your Spring Boot app will run on (default is 8080)
EXPOSE 8080

# Run the Spring Boot application using the actual JAR file name
ENTRYPOINT ["java", "-jar", "/app/eKart-0.0.1-SNAPSHOT.jar"]

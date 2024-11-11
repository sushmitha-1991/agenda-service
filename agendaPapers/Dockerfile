FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY ./target/agendaPapers-0.0.1-SNAPSHOT.jar app.jar

# Make port 8089 available to the world outside this container
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

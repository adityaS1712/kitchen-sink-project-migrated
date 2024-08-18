
# Kitchensink Application

This is a Spring Boot application that uses MongoDB for data storage and Docker for containerization.

## Prerequisites

- Java 22
- Maven
- Docker
- Docker Compose

## Building the Project

1. **Run Maven Build**: Execute the following command to build the project and generate the JAR file.
    ```sh
    mvn clean package
    ```

2. **Verify JAR File**: Ensure that the `kitchensink-1.0.0.jar` file is present in the `target` directory.

## Docker Setup

### Dockerfile

The `Dockerfile` is used to build the Docker image for the application.

```dockerfile
FROM openjdk:22-jdk-slim

WORKDIR /app

COPY target/kitchensink-1.0.0.jar /app/kitchensink.jar

EXPOSE 9081

ENTRYPOINT ["java", "-jar", "kitchensink.jar"]


```
## Build Docker Image

```
docker build -t kitchensink-app .

```

## Run Docker Image

```
docker-compose up --build
```


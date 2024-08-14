
FROM openjdk:22-jdk-slim



WORKDIR /app

COPY target/kitchensink-1.0.0.jar /app/kitchensink.jar

EXPOSE 9081

# Run the jar file
ENTRYPOINT ["java", "-jar", "kitchensink.jar"]
# Use Amazon Corretto 21 JDK as base image
FROM amazoncorretto:21

# Set working directory inside container
WORKDIR /app

# Copy your built JAR file into container
COPY target/moneymanager.jar moneymanger-v1.0.jar

# Activate docker profile (optional, can override at runtime)

# Expose the app port
EXPOSE 9090

# Start the application
ENTRYPOINT ["java", "-jar", "/app/moneymanger-v1.0.jar"]

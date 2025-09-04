# Use an official Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy built jar
COPY --from=build /target/e-commerse-be-0.0.1-SNAPSHOT.jar e-commerse-be.jar

# Expose port Render cung cấp
EXPOSE 8080

# Start Spring Boot với PORT dynamic từ env
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar e-commerse-be.jar"]

# build stage.
FROM maven:3.9.9-eclipse-temurin-24-alpine AS builder

WORKDIR /api-v1

COPY . .

RUN mvn clean install -DskipTests -B

# runtime stage.
FROM openjdk:24-jdk-slim

WORKDIR /api-v1

COPY --from=builder /api-v1/target/api-v1-0.0.1-SNAPSHOT.jar api-v1.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl --fail http://localhost:8080/actuator/health || exit 1
  
ENTRYPOINT [ "java", "-jar", "api-v1.jar" ]
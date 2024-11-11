# Builder stage
FROM openjdk:21-jdk-slim as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

# Final stage
FROM openjdk:21-jdk-slim
WORKDIR application
COPY --from=builder application/application.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080

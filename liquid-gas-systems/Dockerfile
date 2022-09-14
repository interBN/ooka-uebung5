# syntax=docker/dockerfile:1

FROM eclipse-temurin:18-alpine
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]
EXPOSE 8071
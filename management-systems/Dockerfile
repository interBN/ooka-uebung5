# syntax=docker/dockerfile:1

FROM eclipse-temurin:18-alpine
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
EXPOSE 8074
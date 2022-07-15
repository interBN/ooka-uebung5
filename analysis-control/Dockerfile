FROM openjdk:18-jdk-alpine
#ARG JAR_FILE
EXPOSE 8072
COPY target/*jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
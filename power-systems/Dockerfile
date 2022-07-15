FROM openjdk:18-jdk-alpine
EXPOSE 8070
COPY target/*jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
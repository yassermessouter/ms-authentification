FROM openjdk:17-jdk-alpine
COPY target/security*.jar security.jar
ENTRYPOINT ["java", "-jar", "/security.jar"]
EXPOSE 8080
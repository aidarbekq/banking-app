FROM openjdk:17-jdk-slim
VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=target/banking-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

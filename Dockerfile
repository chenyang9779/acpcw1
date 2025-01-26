FROM openjdk:23
LABEL authors="s2693586"
EXPOSE 8080

WORKDIR /app
COPY /target/acp*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
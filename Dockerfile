FROM openjdk:11-jre
WORKDIR /tvds
COPY target/New-TVDS-Backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
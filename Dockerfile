FROM eclipse-temurin:8-jre

WORKDIR /app

COPY target/campus-health-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

CMD ["java", "-jar", "app.jar"]
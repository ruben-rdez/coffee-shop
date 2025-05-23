FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/coffee-shop.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]
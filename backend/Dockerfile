### Stage 1: Build JAR package
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .

### Get dependencies to cache them inside 
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

### Stage 2: Image generation
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

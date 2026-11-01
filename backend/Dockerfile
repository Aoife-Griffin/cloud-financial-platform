
### Stage 1: Build JAR package
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src

RUN mvn clean package -Dmaven.test.skip=true


### Stage 2: Generate runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

### Making a non root application user
RUN addgroup -S app && adduser -S app -G app
COPY --from=build --chown=app:app /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

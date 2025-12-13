FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
WORKDIR /app

COPY --from=build /app/target/kanban-backend-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
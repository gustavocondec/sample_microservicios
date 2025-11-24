FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV PORT=3004
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 3004
ENTRYPOINT ["java", "-jar", "app.jar"]

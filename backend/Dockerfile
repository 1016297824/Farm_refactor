FROM maven:3.8-openjdk-11-slim AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=builder /app/target/Farm-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-XX:MaxMetaspaceSize=96m", "-XX:+UseContainerSupport", "-jar", "app.jar"]

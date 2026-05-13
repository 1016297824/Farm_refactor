FROM openjdk:11-jre-slim

WORKDIR /app

COPY Farm-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx256m", "-Xms256m", "-XX:MaxMetaspaceSize=96m", "-XX:+UseContainerSupport", "-jar", "app.jar"]

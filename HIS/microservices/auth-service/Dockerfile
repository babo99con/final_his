FROM gradle:8.11-jdk17 AS builder
WORKDIR /app

# Copy only the files needed to build to keep the image small
COPY microservices/auth-service/build.gradle microservices/auth-service/settings.gradle microservices/auth-service/gradlew microservices/auth-service/gradlew.bat ./
COPY microservices/auth-service/gradle gradle
COPY microservices/auth-service/src src
COPY util util
RUN sed -i "s|../../util|util|" settings.gradle

RUN gradle bootWar --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends fonts-nanum \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/*.war app.war

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.war"]

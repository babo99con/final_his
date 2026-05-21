FROM eclipse-temurin:17-jdk AS build

ARG SERVICE_DIR
ARG ARTIFACT_NAME

WORKDIR /workspace
COPY . .

WORKDIR /workspace/microservices/${SERVICE_DIR}
RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew \
    && ./gradlew clean assemble -x test --no-daemon \
    && test -f "build/libs/${ARTIFACT_NAME}"

FROM eclipse-temurin:17-jre

ARG SERVICE_DIR
ARG ARTIFACT_NAME

WORKDIR /app
COPY --from=build /workspace/microservices/${SERVICE_DIR}/build/libs/${ARTIFACT_NAME} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

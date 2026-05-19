FROM eclipse-temurin:17-jdk
WORKDIR /app

ARG JAR_FILE=build/libs/Employee-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8022
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-lc","java $JAVA_OPTS -jar /app/app.jar --server.port=8022"]
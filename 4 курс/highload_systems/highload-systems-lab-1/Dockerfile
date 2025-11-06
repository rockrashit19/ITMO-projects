FROM gradle:8.14.3-jdk-21-and-24 AS build

ENV JAVA_HOME=/opt/java/openjdk21

WORKDIR /home/gradle/app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts settings.gradle.kts ./

RUN --mount=type=cache,target=/home/gradle/.gradle \
./gradlew assemble --no-daemon -x test

COPY . .

RUN --mount=type=cache,target=/home/gradle/.gradle \
./gradlew build --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=build/libs/highload_systems_lab_1-0.0.1-SNAPSHOT.jar
COPY --from=build /home/gradle/app/${JAR_FILE} ./app.jar

CMD ["java", "-jar", "app.jar"]
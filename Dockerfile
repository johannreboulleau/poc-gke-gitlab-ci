#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/*.jar

RUN echo "JAR_FILE = $JAR_FILE"

COPY --from=build /home/app/target/*.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]
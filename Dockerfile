FROM openjdk:21-slim

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY build ./build
COPY gradle ./gradle
COPY src ./src

COPY ./build/libs/gwangju-talent-festival-slogan-server-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar" ]
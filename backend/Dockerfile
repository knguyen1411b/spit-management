FROM gradle:8.14.3-jdk21-alpine AS build

WORKDIR /home/gradle/src

COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon

COPY . .

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine as prod

WORKDIR /backend

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
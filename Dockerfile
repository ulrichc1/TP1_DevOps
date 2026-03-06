FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradle ./gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/Hello-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

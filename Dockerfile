FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradle ./gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
COPY src ./src

# Stage test : exécute les tests unitaires
FROM build AS test
RUN ./gradlew test

# Stage production : build le JAR (tests skippés, déjà validés dans le stage test)
FROM build AS package
RUN ./gradlew build -x test

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
COPY --from=package /app/build/libs/Hello-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3-eclipse-temurin-21-alpine as BUILDER

WORKDIR /build

COPY src ./src
COPY pom.xml pom.xml

RUN mvn clean compile assembly:single && \
    mv ./target/dai-udp-musician-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar

FROM eclipse-temurin:21-jre-alpine

COPY --from=BUILDER /build/app.jar ./

# Run the jar file with the instrument as a command-line argument
ENTRYPOINT ["java", "-jar", "./app.jar"]

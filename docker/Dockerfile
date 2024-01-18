FROM maven:3-eclipse-temurin-21-alpine as BUILDER

WORKDIR /build

COPY ./src src
COPY pom.xml pom.xml

RUN mvn clean compile assembly:single

FROM eclipse-temurin:21-jre-alpine

COPY --from=BUILDER /build/target/*.jar ./

CMD java -jar *.jar

EXPOSE 7000
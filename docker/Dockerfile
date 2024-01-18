FROM maven:3-eclipse-temurin-21-alpine as BUILDER

# Copy shared code and build it first
WORKDIR /build/shared

COPY ./shared-code ./
RUN mvn clean package

ARG SOURCE_PATH

WORKDIR /build

# Copy specified application
COPY $SOURCE_PATH ./

RUN mvn clean compile assembly:single

FROM eclipse-temurin:21-jre-alpine

COPY --from=BUILDER /build/target/*.jar ./

CMD java -jar *.jar

EXPOSE 7000
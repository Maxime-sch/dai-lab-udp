# Start with a base image containing Java runtime
FROM maven:3-eclipse-temurin-21-alpine as BUILDER

# Add Maintainer Info
LABEL maintainer="maxime-sch@example.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 2205 available to the world outside this container
EXPOSE 2205

# The application's jar file
ARG JAR_FILE=target/musician-application.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file with the instrument as a command-line argument
ENTRYPOINT ["java","-jar","/app.jar"]

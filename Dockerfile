FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk

ENV SPRING_PROFILES_ACTIVE=ssl
ENV JAVA_OPTS=""

RUN mkdir /certs

# Copy the certificates into the Docker image
COPY ./server_certs /certs

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with SSL options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
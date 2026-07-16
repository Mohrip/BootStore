# Stage 1: Build the application and create the JAR file
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B



## here i wrote notes why i choose 2 stages not 1 -> based on my research:

## I chose a multi stage build to the final image size
## By using a separate build stage, we can include all the necessary dependencies for building the application without Expansion the final runtime image
## The final image only contains the compiled JAR file and the necessary runtime environment,
## resulting in a smaller and more efficient image for the final app deployment

# Stage 2: Here we run the application using a lightweight JRE image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
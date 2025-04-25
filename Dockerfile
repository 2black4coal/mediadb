# Step 1: Build the JAR with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the JAR in a smaller image
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/media-1.0-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]

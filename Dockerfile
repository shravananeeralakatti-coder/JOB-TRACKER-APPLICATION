# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# ----------- Run Stage -----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the executable jar from build stage
# We use a wildcard but ensure we get the main one by excluding common extras if necessary, 
# or just rely on the fact that we'll rename it in the next step or use a specific pattern.
COPY --from=build /app/target/job_tracker_app-0.0.1-SNAPSHOT.jar app.jar

# Ensure the app listens on the port Render provides
ENV PORT=8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]

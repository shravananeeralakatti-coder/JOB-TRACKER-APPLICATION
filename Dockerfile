# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# ----------- Run Stage -----------
# Using the full JRE instead of alpine for better compatibility with native libraries
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the executable jar from build stage
# Using a wildcard to be safer with the exact filename
COPY --from=build /app/target/job_tracker_app-*.jar app.jar

# Ensure the app listens on the port Render provides
ENV PORT=8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","app.jar"]

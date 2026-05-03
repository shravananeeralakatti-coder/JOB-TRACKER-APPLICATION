# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# ----------- Run Stage -----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the executable jar from build stage
COPY --from=build /app/target/job_tracker_app-*.jar app.jar

# Ensure the app listens on the port Render provides
ENV PORT=10000
EXPOSE 10000

# Run the application with specific port mapping
CMD java -Dserver.port=${PORT} -jar app.jar

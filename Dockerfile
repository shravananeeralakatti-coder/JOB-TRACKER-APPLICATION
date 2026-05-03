# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and source code directly from the root
COPY pom.xml .
COPY src ./src

# Build the jar file
RUN mvn clean package -DskipTests

# ----------- Run Stage -----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy jar from build stage. The jar is produced in /app/target/
COPY --from=build /app/target/*.jar app.jar

# Run the application
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

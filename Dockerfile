# ----------- Build Stage -----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy entire project (your project is inside "demo" folder)
COPY demo ./demo

# Move into project directory
WORKDIR /app/demo

# Build the jar file
RUN mvn clean package -DskipTests


# ----------- Run Stage -----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/demo/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
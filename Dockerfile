#
# Build stage
#
FROM maven:4.0.0-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/CarDatabaseWeek6-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
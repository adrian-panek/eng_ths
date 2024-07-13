FROM maven:3.9.8-eclipse-temurin-21-alpine
WORKDIR /app
COPY . /app
RUN mvn clean -DskipTests install
ENTRYPOINT ["java", "-jar", "target/project-0.0.1-SNAPSHOT.jar"]

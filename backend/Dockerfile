FROM maven:3.9.8-eclipse-temurin-21-alpine as builder
WORKDIR /build
COPY . /build
RUN mvn clean -DskipTests compile package
RUN mv ./target/project-0.0.1-SNAPSHOT.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract
FROM eclipse-temurin:21.0.3_9-jdk-alpine
WORKDIR /app
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./
CMD ["java", "org.springframework.boot.loader.launch.JarLauncher"]

#ENTRYPOINT ["java", "-jar", "target/project-0.0.1-SNAPSHOT.jar"]

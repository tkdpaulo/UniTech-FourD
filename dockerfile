FROM maven:3.8.7 as builder
WORKDIR /app
COPY ./pom.xml /app/pom.xml
COPY ./src /app/src
RUN mvn package -DskipTests

FROM eclipse-temurin:18-jdk-alpine as final
WORKDIR /app
ENV ALLOW_ORIGIN=""
COPY --from=builder /app/target/unitech.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c","java -jar ${JAVA_OPS} /app/app.jar"]
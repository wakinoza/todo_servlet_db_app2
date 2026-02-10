
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17-temurin
WORKDIR /usr/local/tomcat

COPY --from=builder /app/target/todo.war webapps/todo.war


COPY context.xml conf/context.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]
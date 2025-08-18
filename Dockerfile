# syntax=docker/dockerfile:1
# ====== BUILD ======
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Cache de dependências e plugins (sem compilar ainda)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests dependency:resolve dependency:resolve-plugins

# Agora copia o código e compila
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -DskipTests package

# ====== RUNTIME ======
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

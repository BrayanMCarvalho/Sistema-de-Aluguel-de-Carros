FROM node:22-alpine AS frontend
WORKDIR /build/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-25 AS backend
WORKDIR /build/backend
COPY backend/pom.xml ./
RUN mvn -B -ntp dependency:go-offline -DskipTests
COPY backend/src/ ./src/
COPY --from=frontend /build/frontend/dist/ ./src/main/resources/static/
RUN mvn -B -ntp package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=backend --chown=10001:10001 /build/backend/target/locar-0.1.0.jar ./app.jar
ENV SERVER_PORT=10000
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=60 -Duser.timezone=America/Sao_Paulo"
USER 10001
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

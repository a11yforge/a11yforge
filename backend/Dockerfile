FROM maven:3-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY backend/pom.xml .
RUN mvn dependency:go-offline
COPY backend/src ./src
RUN mvn package -DskipTests

FROM node:24 AS scanner-build
WORKDIR /scanner
COPY scanner/package.json scanner/package-lock.json ./
RUN npm ci
COPY scanner/ .
RUN npm run build

FROM mcr.microsoft.com/playwright:v1.59.1-jammy
COPY --from=eclipse-temurin:21-jre /opt/java/openjdk /opt/java/openjdk
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
WORKDIR /app
COPY --from=backend-build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar
COPY --from=scanner-build /scanner /scanner
ENV SCANNER_CLI_PATH=/scanner/dist/cli.js
ENV VERIFIER_CLI_PATH=/scanner/dist/cli-verify.js
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

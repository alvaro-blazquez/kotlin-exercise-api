FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY app/build/libs libs/
COPY app/build/classes classes/
ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "org.energy.AppKt"]
EXPOSE 8080

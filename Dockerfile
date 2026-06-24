# Etapa 1: Construcción (Builder)
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y construimos el JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos solo el JAR compilado de la etapa anterior
COPY --from=builder /app/target/ms.contenedores-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]

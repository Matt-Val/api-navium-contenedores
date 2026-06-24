# Imagen
FROM eclipse-temurin:17-jdk-alpine

# Copiamos el JAR generado 
COPY target/ms.contenedores-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

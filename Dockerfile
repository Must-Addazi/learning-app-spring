# Utiliser une image officielle de Java comme base
FROM openjdk:21-jdk-slim


# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR de l'application dans le conteneur
COPY target/*.jar app.jar

# Exposer le port 8080
EXPOSE 8080

# Lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]

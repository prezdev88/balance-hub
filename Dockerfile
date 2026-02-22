FROM eclipse-temurin:25-jdk

# Imagen para ejecutar la app con Maven Wrapper dentro de Docker Compose.
# Incluye curl/bash porque el wrapper puede necesitarlos para descargar Maven.
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        bash \
        curl \
        ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

EXPOSE 8080

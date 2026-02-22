FROM eclipse-temurin:25-jdk

# Imagen base con JDK 25.
# Instalamos utilidades de desarrollo que VS Code espera (especialmente git)
# para evitar el error de "Git not found" en Source Control al iniciar.
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        git \
        openssh-client \
        fish \
        curl \
        ca-certificates \
        procps \
        bash-completion \
    && rm -rf /var/lib/apt/lists/*

# Directorio de trabajo dentro del contenedor (coincide con docker-compose.yml).
WORKDIR /workspace

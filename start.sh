#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

if ! command -v docker >/dev/null 2>&1; then
  echo "Error: docker no está instalado o no está en PATH."
  exit 1
fi

if ! command -v ./mvnw >/dev/null 2>&1; then
  echo "Error: no se encontró ./mvnw en $SCRIPT_DIR"
  exit 1
fi

echo "Levantando PostgreSQL (docker compose)..."
docker compose up -d db

echo "Esperando PostgreSQL..."
for _ in $(seq 1 30); do
  if docker exec balance-hub-db pg_isready -U balancehub -d balance_hub >/dev/null 2>&1; then
    echo "PostgreSQL listo."
    break
  fi
  sleep 1
done

echo "Iniciando backend con Maven (Flyway debería ejecutar migraciones al arrancar)..."
./mvnw clean spring-boot:run

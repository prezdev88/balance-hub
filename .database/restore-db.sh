#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 1 ]]; then
  echo "Uso: $0 <archivo_backup.dump>" >&2
  exit 1
fi

BACKUP_FILE="$1"

if [[ ! -f "$BACKUP_FILE" ]]; then
  echo "Error: no existe el archivo '$BACKUP_FILE'." >&2
  exit 1
fi

DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-balance_hub}"
DB_USER="${DB_USER:-balancehub}"
DB_PASSWORD="${DB_PASSWORD:-balancehub}"
SYSTEM_DB="${SYSTEM_DB:-postgres}"
AUTO_CREATE_DB="${AUTO_CREATE_DB:-true}"

if ! command -v pg_restore >/dev/null 2>&1; then
  echo "Error: pg_restore no está instalado." >&2
  echo "Instala postgresql-client y vuelve a intentar." >&2
  exit 1
fi

if ! command -v psql >/dev/null 2>&1; then
  echo "Error: psql no está instalado." >&2
  echo "Instala postgresql-client y vuelve a intentar." >&2
  exit 1
fi

if [[ "$AUTO_CREATE_DB" == "true" ]]; then
  echo "Verificando existencia de base de datos '${DB_NAME}'..."
  DB_EXISTS=$(PGPASSWORD="$DB_PASSWORD" psql \
    --host="$DB_HOST" \
    --port="$DB_PORT" \
    --username="$DB_USER" \
    --dbname="$SYSTEM_DB" \
    --tuples-only --no-align \
    --command="SELECT 1 FROM pg_database WHERE datname = '${DB_NAME}';" | tr -d '[:space:]')

  if [[ "$DB_EXISTS" != "1" ]]; then
    echo "Base de datos '${DB_NAME}' no existe. Creando..."
    PGPASSWORD="$DB_PASSWORD" psql \
      --host="$DB_HOST" \
      --port="$DB_PORT" \
      --username="$DB_USER" \
      --dbname="$SYSTEM_DB" \
      --command="CREATE DATABASE \"${DB_NAME}\";"
  fi
fi

echo "Restaurando backup '$BACKUP_FILE' en '${DB_NAME}'..."
PGPASSWORD="$DB_PASSWORD" pg_restore \
  --verbose \
  --clean \
  --if-exists \
  --no-owner \
  --no-privileges \
  --host="$DB_HOST" \
  --port="$DB_PORT" \
  --username="$DB_USER" \
  --dbname="$DB_NAME" \
  "$BACKUP_FILE"

echo "Restore completado en base de datos '${DB_NAME}'."

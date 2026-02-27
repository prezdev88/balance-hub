#!/usr/bin/env bash
set -euo pipefail

DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-balance_hub}"
DB_USER="${DB_USER:-balancehub}"
DB_PASSWORD="${DB_PASSWORD:-balancehub}"
BACKUP_DIR="${BACKUP_DIR:-./.database/backups}"

if ! command -v pg_dump >/dev/null 2>&1; then
  echo "Error: pg_dump no está instalado." >&2
  echo "Instala postgresql-client y vuelve a intentar." >&2
  exit 1
fi

TIMESTAMP="$(date +%Y%m%d_%H%M%S)"
OUTPUT_FILE="${1:-${BACKUP_DIR}/${DB_NAME}_${TIMESTAMP}.dump}"

mkdir -p "$(dirname "$OUTPUT_FILE")"

echo "Creando backup de '${DB_NAME}' en '${OUTPUT_FILE}'..."
PGPASSWORD="$DB_PASSWORD" pg_dump \
  --format=custom \
  --blobs \
  --verbose \
  --no-owner \
  --no-privileges \
  --host="$DB_HOST" \
  --port="$DB_PORT" \
  --username="$DB_USER" \
  --dbname="$DB_NAME" \
  --file="$OUTPUT_FILE"

echo "Backup completado: $OUTPUT_FILE"

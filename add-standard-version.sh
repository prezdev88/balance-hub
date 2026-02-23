#!/usr/bin/env bash
set -euo pipefail

# add-standard-version.sh
# Configura standard-version en el directorio actual (idempotente).
#
# Qué hace:
# 1) Crea package.json si no existe (npm init -y)
# 2) Instala standard-version como devDependency
# 3) Agrega script "release": "standard-version"
# 4) Crea .versionrc.json si no existe (incluyendo URLs de repo detectadas desde git)
# 5) Agrega node_modules y package-lock.json a .gitignore (si faltan)
#
# Opciones:
#   --force-versionrc     Sobrescribe .versionrc.json si ya existe
#   --keep-package-lock   No agrega package-lock.json a .gitignore

FORCE_VERSIONRC=false
KEEP_PACKAGE_LOCK=false

for arg in "$@"; do
  case "$arg" in
    --force-versionrc)
      FORCE_VERSIONRC=true
      ;;
    --keep-package-lock)
      KEEP_PACKAGE_LOCK=true
      ;;
    -h|--help)
      sed -n '1,40p' "$0" | sed 's/^# \{0,1\}//'
      exit 0
      ;;
    *)
      echo "Opción no soportada: $arg" >&2
      exit 1
      ;;
  esac
done

if ! command -v npm >/dev/null 2>&1; then
  echo "Error: npm no está instalado o no está en PATH." >&2
  exit 1
fi

echo "==> Verificando package.json..."
if [ ! -f package.json ]; then
  echo "package.json no existe. Ejecutando npm init -y"
  npm init -y >/dev/null
else
  echo "package.json encontrado."
fi

echo "==> Instalando standard-version (devDependency)..."
npm install --save-dev standard-version

echo "==> Configurando script release en package.json..."
npm pkg set scripts.release="standard-version" >/dev/null

append_gitignore_line() {
  local line="$1"
  local file=".gitignore"

  if [ ! -f "$file" ]; then
    touch "$file"
  fi

  if ! grep -Fxq "$line" "$file"; then
    printf '%s\n' "$line" >> "$file"
    echo "Agregado a .gitignore: $line"
  else
    echo "Ya existe en .gitignore: $line"
  fi
}

detect_repo_urls_json_fields() {
  if ! command -v git >/dev/null 2>&1; then
    return 0
  fi

  local remote
  remote="$(git remote get-url origin 2>/dev/null || true)"
  if [ -z "$remote" ]; then
    return 0
  fi

  # Normaliza formas comunes:
  #   git@github.com:owner/repo.git
  #   ssh://git@github.com/owner/repo.git
  #   https://github.com/owner/repo.git
  local host path repo_path

  if [[ "$remote" =~ ^git@([^:]+):(.+)$ ]]; then
    host="${BASH_REMATCH[1]}"
    path="${BASH_REMATCH[2]}"
  elif [[ "$remote" =~ ^ssh://git@([^/]+)/(.+)$ ]]; then
    host="${BASH_REMATCH[1]}"
    path="${BASH_REMATCH[2]}"
  elif [[ "$remote" =~ ^https?://([^/]+)/(.+)$ ]]; then
    host="${BASH_REMATCH[1]}"
    path="${BASH_REMATCH[2]}"
  else
    return 0
  fi

  repo_path="${path%.git}"
  repo_path="${repo_path#/}"

  local base commit_url compare_url
  case "$host" in
    github.com)
      base="https://$host/$repo_path"
      commit_url="$base/commit/{{hash}}"
      compare_url="$base/compare/{{previousTag}}...{{currentTag}}"
      ;;
    gitlab.com|gitlab.*)
      base="https://$host/$repo_path"
      commit_url="$base/-/commit/{{hash}}"
      compare_url="$base/-/compare/{{previousTag}}...{{currentTag}}"
      ;;
    bitbucket.org|bitbucket.*)
      base="https://$host/$repo_path"
      commit_url="$base/commits/{{hash}}"
      compare_url="$base/branches/compare/{{currentTag}}..{{previousTag}}"
      ;;
    *)
      return 0
      ;;
  esac

  printf ',\n  "commitUrlFormat": "%s",\n  "compareUrlFormat": "%s"' "$commit_url" "$compare_url"
}

echo "==> Actualizando .gitignore..."
append_gitignore_line "node_modules"
if [ "$KEEP_PACKAGE_LOCK" = false ]; then
  append_gitignore_line "package-lock.json"
else
  echo "Se mantiene package-lock.json fuera de .gitignore (--keep-package-lock)"
fi

VERSIONRC_FILE=".versionrc.json"
if [ ! -f "$VERSIONRC_FILE" ] || [ "$FORCE_VERSIONRC" = true ]; then
  echo "==> Creando $VERSIONRC_FILE..."
  REPO_URL_FIELDS="$(detect_repo_urls_json_fields || true)"
  cat > "$VERSIONRC_FILE" <<'JSON'
{
  "types": [
    { "type": "feat", "section": "Features" },
    { "type": "fix", "section": "Bug Fixes" },
    { "type": "refactor", "section": "Refactor" },
    { "type": "style", "section": "Style" },
    { "type": "test", "section": "Tests" },
    { "type": "chore", "hidden": true },
    { "type": "docs", "hidden": true },
    { "type": "perf", "hidden": true }
  ]
}
JSON
  # Inserta URLs detectadas de git al final del objeto JSON (si aplica).
  if [ -n "${REPO_URL_FIELDS:-}" ]; then
    # Reemplaza el cierre final para añadir los campos.
    tmp_file="$(mktemp)"
    sed '$s/}/'"$REPO_URL_FIELDS"'
}/' "$VERSIONRC_FILE" > "$tmp_file"
    mv "$tmp_file" "$VERSIONRC_FILE"
    echo "==> URLs de repo detectadas desde git remote origin"
  else
    echo "==> No se detectaron URLs de repo compatibles (GitHub/GitLab/Bitbucket)."
  fi
else
  echo "==> $VERSIONRC_FILE ya existe. No se sobrescribe."
fi

echo
echo "Listo."
echo "Siguiente paso: npm run release"

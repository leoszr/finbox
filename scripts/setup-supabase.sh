#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APP_DIR="$ROOT/finance-app"
ENV_FILE="$APP_DIR/.env"

ask() {
  local name="$1" prompt="$2" default="${3:-}" secret="${4:-false}" value
  if [[ -n "$default" ]]; then
    prompt="$prompt [$default]"
  fi
  if [[ "$secret" == "true" ]]; then
    read -r -s -p "$prompt: " value; echo >&2
  else
    read -r -p "$prompt: " value
  fi
  value="${value:-$default}"
  if [[ -z "$value" ]]; then
    echo "Erro: $name obrigatório." >&2
    exit 1
  fi
  printf '%s' "$value"
}

extract_ref() {
  local input="$1"
  input="${input#https://}"
  input="${input#http://}"
  input="${input%%.supabase.co*}"
  printf '%s' "$input"
}

cat <<'MSG'
Setup Supabase Auth + produção local.
Cole dados do Supabase/Postgres quando pedir.

Onde achar SUPABASE_PROJECT_REF:
- Project URL: https://<ref>.supabase.co
- Pode colar URL inteira ou só ref.
MSG

echo
SUPABASE_INPUT=$(ask SUPABASE_PROJECT_REF "Supabase Project URL ou ref")
SUPABASE_PROJECT_REF=$(extract_ref "$SUPABASE_INPUT")
POSTGRES_DB=$(ask POSTGRES_DB "Nome DB" "finance_app")
POSTGRES_USER=$(ask POSTGRES_USER "Usuário Postgres" "finance_app")
POSTGRES_PASSWORD=$(ask POSTGRES_PASSWORD "Senha Postgres" "" true)
API_PORT=$(ask API_PORT "Porta API host" "8080")
SERVER_PORT="8080"

cat > "$ENV_FILE" <<EOF
SPRING_PROFILES_ACTIVE=prod,supabase
SERVER_PORT=$SERVER_PORT
API_PORT=$API_PORT

POSTGRES_DB=$POSTGRES_DB
POSTGRES_USER=$POSTGRES_USER
POSTGRES_PASSWORD=$POSTGRES_PASSWORD

DATABASE_URL=jdbc:postgresql://postgres:5432/$POSTGRES_DB
DATABASE_USERNAME=$POSTGRES_USER
DATABASE_PASSWORD=$POSTGRES_PASSWORD

SUPABASE_PROJECT_REF=$SUPABASE_PROJECT_REF
EOF

chmod 600 "$ENV_FILE"

echo
printf 'Criado: %s\n' "$ENV_FILE"
echo
cat <<EOF
Próximos comandos:
  cd "$APP_DIR"
  docker compose -f docker-compose.prod.yml --env-file .env up -d --build

Teste backend:
  curl http://localhost:$API_PORT/actuator/health

Teste auth real:
  1. Faça login no frontend Supabase.
  2. Copie session.access_token.
  3. Rode:
     curl http://localhost:$API_PORT/me -H "Authorization: Bearer <ACCESS_TOKEN>"
EOF

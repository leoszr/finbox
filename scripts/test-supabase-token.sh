#!/usr/bin/env bash
set -euo pipefail

API_URL="${API_URL:-http://localhost:8080}"
TOKEN="${1:-}"
if [[ -z "$TOKEN" ]]; then
  read -r -p "Cole Supabase access_token: " TOKEN
fi

if [[ -z "$TOKEN" ]]; then
  echo "Token vazio." >&2
  exit 1
fi

echo "GET $API_URL/me"
curl -i "$API_URL/me" -H "Authorization: Bearer $TOKEN"
echo

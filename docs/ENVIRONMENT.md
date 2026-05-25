# ENVIRONMENT.md — Variáveis de ambiente

## API

| Variável | Obrigatória | Exemplo | Uso |
|---|---:|---|---|
| `SPRING_PROFILES_ACTIVE` | sim | `prod,supabase` | Profiles Spring ativos. Use `local`/`test` para mock. |
| `SERVER_PORT` | não | `8080` | Porta interna da API. |
| `API_PORT` | não | `8080` | Porta publicada no `docker-compose.prod.yml`. |

## PostgreSQL

| Variável | Obrigatória | Exemplo | Uso |
|---|---:|---|---|
| `DATABASE_URL` | sim | `jdbc:postgresql://postgres:5432/finance_app` | JDBC URL usada pela API. |
| `DATABASE_USERNAME` | sim | `finance_app` | Usuário do banco. |
| `DATABASE_PASSWORD` | sim | `change-me` | Senha do banco. |
| `POSTGRES_DB` | sim no compose | `finance_app` | Nome do banco no container Postgres. |
| `POSTGRES_USER` | sim no compose | `finance_app` | Usuário criado no Postgres. |
| `POSTGRES_PASSWORD` | sim no compose | `change-me` | Senha criada no Postgres. |

## Supabase Auth

| Variável | Obrigatória | Exemplo | Uso |
|---|---:|---|---|
| `SUPABASE_PROJECT_REF` | sim com profile `supabase` | `abcdefghijklmnopqrst` | Ref do projeto Supabase usado para JWKS/issuer. |

Frontend deve enviar o token Supabase:

```http
Authorization: Bearer <supabase_access_token>
```

Backend valida JWT em:

```text
https://<SUPABASE_PROJECT_REF>.supabase.co/auth/v1/.well-known/jwks.json
```

Valida `issuer=https://<ref>.supabase.co/auth/v1` e `audience=authenticated`.

## Firebase legado

| Variável | Obrigatória | Exemplo | Uso |
|---|---:|---|---|
| `FIREBASE_PROJECT_ID` | só se implementar provider Firebase real | `finbox-prod` | Projeto Firebase. |
| `FIREBASE_CREDENTIALS_PATH` | não | `/run/secrets/firebase.json` | Caminho do JSON de service account. |
| `FIREBASE_CREDENTIALS_JSON` | não | `{...}` | JSON de service account via env. |

## Local

- Profile local: `SPRING_PROFILES_ACTIVE=local`.
- Docker local usa `docker-compose.yml`.
- Nunca usar `MockAuthTokenService` em `prod`; ele só carrega em `local` e `test`.

## Produção base

1. Criar `.env` fora do git com valores reais.
2. Rodar:

```bash
docker compose -f docker-compose.prod.yml --env-file .env up -d --build
```

3. Expor API via Nginx externo com TLS.

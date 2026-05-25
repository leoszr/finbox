# SUPABASE_AUTH.md — Como usar Supabase Auth

## Script semi-automático

Rode na raiz do repo:

```bash
./scripts/setup-supabase.sh
```

Você só cola:

- Project URL/ref do Supabase.
- Nome/user/senha do Postgres.
- Porta da API.

O script cria `finance-app/.env` e mostra comandos de start.

Para testar token real:

```bash
./scripts/test-supabase-token.sh
```

ou:

```bash
./scripts/test-supabase-token.sh '<ACCESS_TOKEN>'
```

## Backend

Já implementado:

- `AuthTokenService` provider-neutral.
- `MockAuthTokenService` apenas em `local` e `test`.
- `SupabaseAuthTokenService` em profile `supabase`.
- Validação JWT via JWKS:
  - `https://<SUPABASE_PROJECT_REF>.supabase.co/auth/v1/.well-known/jwks.json`
- Valida issuer e audience `authenticated`.

## Variáveis

```env
SPRING_PROFILES_ACTIVE=prod,supabase
SUPABASE_PROJECT_REF=seu-project-ref
```

`project-ref` aparece na URL do projeto:

```text
https://<project-ref>.supabase.co
```

## Frontend

Depois do login, enviar `access_token` para API:

```http
Authorization: Bearer <access_token>
```

Exemplo JS:

```js
const { data } = await supabase.auth.getSession()
const token = data.session?.access_token

await fetch(`${API_URL}/me`, {
  headers: { Authorization: `Bearer ${token}` }
})
```

## Importante

- Não envie `anon key` como bearer para sua API.
- Use sempre `session.access_token`.
- Se trocar projeto Supabase, usuários terão novo `sub`; backend criará novos usuários internos.

# T0002 — Criar Docker Compose local do PostgreSQL

## Épico 0 — Setup do projeto

**Descrição**: Adicionar `docker-compose.yml` com PostgreSQL 16 local.

**TDD**:
- Não se aplica unitário; validar com comando de subida local.

**Aceite**:
- Serviço `postgres` usa database/user/password de desenvolvimento.
- Porta `5432` exposta.
- Volume persistente configurado.

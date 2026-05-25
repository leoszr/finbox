# T0106 — Criar migration `users` e `user_preferences`

## Épico 1 — Segurança e usuário

**Descrição**: Criar tabelas conforme PRD.

**TDD**:
- `MigrationIT` sobe PostgreSQL e roda Flyway.

**Aceite**:
- Campos e defaults corretos.
- `firebase_uid` único.
- FK preferences -> users.

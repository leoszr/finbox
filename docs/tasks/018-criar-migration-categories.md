# T0203 — Criar migration `categories`

## Épico 2 — Categorias

**Descrição**: Criar tabela e índice único por usuário/nome normalizado.

**TDD**:
- `CategoryMigrationIT` valida constraint real.

**Aceite**:
- `user_id` obrigatório.
- Índice `uk_category_name_per_user` existe.

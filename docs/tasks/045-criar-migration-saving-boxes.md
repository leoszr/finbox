# T0501 — Criar migration `saving_boxes`

## Épico 5 — Caixas

**Descrição**: Criar tabela, soft delete e índices parciais para caixas BRL.

**TDD**:
- `SavingBoxMigrationIT` valida unicidade ativa e default por usuário.

**Aceite**:
- `deleted_at` permite recriar nome após exclusão.
- Uma default ativa por usuário.
- Moeda fixa `BRL` no MVP.

# T0303 — Criar migration `transactions`

## Épico 3 — Transações

**Descrição**: Criar tabela e FKs de transações.

**TDD**:
- `TransactionMigrationIT` valida schema e FKs.

**Aceite**:
- `category_id` obrigatório.
- `box_id` opcional.
- Campos de valor, tipo e data são obrigatórios.

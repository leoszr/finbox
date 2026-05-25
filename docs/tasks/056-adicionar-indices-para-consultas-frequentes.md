# T0701 — Adicionar índices para consultas frequentes

## Épico 7 — Qualidade, hardening e deploy futuro

**Descrição**: Criar índices para histórico e dashboard.

**TDD**:
- `MigrationIT` valida execução.

**Aceite**:
- Índices em `transactions(user_id, transaction_date)`, `transactions(user_id, category_id)`, `saving_boxes(user_id)`.

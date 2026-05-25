# T0402 — Criar migrations de budget

## Épico 4 — Budget

**Descrição**: Criar `budgets` e `budget_categories`.

**TDD**:
- `BudgetMigrationIT` valida constraints.

**Aceite**:
- Um budget ativo por usuário via índice parcial.
- Uma categoria por budget via índice único.

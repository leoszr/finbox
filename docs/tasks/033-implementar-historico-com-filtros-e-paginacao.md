# T0309 — Implementar histórico com filtros e paginação

## Épico 3 — Transações

**Descrição**: `GET /transactions` com filtros do PRD.

**TDD**:
- `TransactionServiceTest.filterAndSortHistory()`.

**Aceite**:
- Default sort `NEWEST_FIRST`.
- Default page 0 size 20.
- Filtros combináveis.

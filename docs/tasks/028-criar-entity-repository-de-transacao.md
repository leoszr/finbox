# T0304 — Criar entity/repository de transação

## Épico 3 — Transações

**Descrição**: Implementar `Transaction` e queries filtradas.

**TDD**:
- `TransactionRepositoryIT`: `findByIdAndUserId`, filtros básicos, paginação.

**Aceite**:
- Busca por ID sempre inclui `user_id`.
- Filtros por data, categoria, tipo, valor, descrição.

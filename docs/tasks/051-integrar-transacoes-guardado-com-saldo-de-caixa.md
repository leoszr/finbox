# T0507 — Integrar transações “Guardado” com saldo de caixa

## Épico 5 — Caixas

**Descrição**: Criar/editar/excluir transações com categoria `SAVED` alterando saldo.

**TDD**:
- `TransactionServiceTest.savedCategoryUpdatesBoxBalance()`.

**Aceite**:
- Receita aumenta saldo.
- Despesa diminui saldo.
- Despesa não deixa negativo.
- Categoria `SAVED` exige `box_id`.
- Categoria não `SAVED` exige `box_id = null`.
- Operação transacional com lock.

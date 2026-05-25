# T0502 — Criar entity/repository de caixas

## Épico 5 — Caixas

**Descrição**: Implementar `SavingBox` e queries com lock.

**TDD**:
- `SavingBoxRepositoryIT`: busca ativa, busca com lock, default do usuário.

**Aceite**:
- Queries ignoram deleted por padrão.
- Métodos com `@Lock(PESSIMISTIC_WRITE)` para saldo.

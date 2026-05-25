# T0506 — Implementar exclusão lógica de caixa

## Épico 5 — Caixas

**Descrição**: Soft delete caixa custom.

**TDD**:
- `SavingBoxServiceTest.softDeleteBox()`.

**Aceite**:
- Default não pode ser deletada.
- Caixa com histórico permanece para consistência.
- Lista não retorna deletadas.

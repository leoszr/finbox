# T0208 — Implementar exclusão de categoria custom

## Épico 2 — Categorias

**Descrição**: Excluir categoria custom e mover transações para categoria destino, padrão “Não categorizado”.

**TDD**:
- `CategoryServiceTest.deleteMovesTransactions()`.

**Aceite**:
- Especial não pode ser excluída.
- Transações são reclassificadas antes da exclusão.
- Categoria destino pertence ao mesmo usuário.

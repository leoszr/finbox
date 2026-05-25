# T0308 — Implementar exclusão de transação comum

## Épico 3 — Transações

**Descrição**: Excluir transação do usuário.

**TDD**:
- `TransactionServiceTest.deleteCommonTransaction()`.

**Aceite**:
- Não exclui transação de outro usuário.
- Retorna 204 via endpoint.

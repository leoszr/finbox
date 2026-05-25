# T0305 — Implementar criação de transação comum

## Épico 3 — Transações

**Descrição**: Criar receita/despesa com categoria custom ou “Não categorizado”.

**TDD**:
- `TransactionServiceTest.createCommonTransaction()`.

**Aceite**:
- Sem categoria usa `UNCATEGORIZED`.
- Valor zero/negativo rejeitado.
- Categoria pertence ao usuário.
- Categoria não “Guardado” exige `box_id = null`.

# T0204 — Criar entity/repository de categoria

## Épico 2 — Categorias

**Descrição**: Implementar `Category` e `CategoryRepository`.

**TDD**:
- `CategoryRepositoryIT`: `findByIdAndUserId`, `existsByUserIdAndNormalizedName`.

**Aceite**:
- Nenhum método de service usa `findById` simples.
- Busca por categorias especiais suportada.

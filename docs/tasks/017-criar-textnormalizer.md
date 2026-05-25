# T0202 — Criar `TextNormalizer`

## Épico 2 — Categorias

**Descrição**: Normalizar nomes para unicidade ignorando caixa, acentos e espaços extras.

**TDD**:
- `TextNormalizerTest`: `Alimentação`, `alimentacao`, espaços múltiplos.

**Aceite**:
- Mesmo nome lógico gera mesmo `normalized_name`.
- String vazia após trim é inválida.

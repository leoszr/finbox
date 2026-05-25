# T0004 — Criar padrão de erros da API

## Épico 0 — Setup do projeto

**Descrição**: Implementar `ApiExceptionHandler`, exceções comuns e `ApiErrorResponse`.

**TDD**:
- `ApiExceptionHandlerTest` para validation, not found, business rule, unauthorized.

**Aceite**:
- Resposta contém `timestamp`, `status`, `error`, `message`, `path`.
- Erros de Bean Validation viram `VALIDATION_ERROR`.
- Business rules viram 400.
- Not found vira 404.

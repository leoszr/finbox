# T0101 — Criar DTO/token interno Firebase

## Épico 1 — Segurança e usuário

**Descrição**: Criar tipo interno com `firebaseUid`, `email`, `name`.

**TDD**:
- Teste de construção/validação do DTO.

**Aceite**:
- Campos obrigatórios validados.
- Sem dependência direta de classe do Firebase fora da camada security.

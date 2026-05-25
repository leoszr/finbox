# T0103 — Criar `AuthenticatedUser`

## Épico 1 — Segurança e usuário

**Descrição**: Representar usuário autenticado no Spring Security.

**TDD**:
- `AuthenticatedUserTest` valida authorities e campos.

**Aceite**:
- Contém `userId`, `firebaseUid`, `email`, `name`.
- Pode ser usado como principal no contexto.

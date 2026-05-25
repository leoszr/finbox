# T0110 — Implementar `PATCH /me`

## Épico 1 — Segurança e usuário

**Descrição**: Atualizar nome, moeda primária, tema e face id.

**TDD**:
- `UserServiceTest.updateProfile()`.

**Aceite**:
- Validação de tamanho e enums.
- Não altera `firebase_uid` nem email vindo do token.

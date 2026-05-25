# T0109 — Implementar `GET /me`

## Épico 1 — Segurança e usuário

**Descrição**: Retornar perfil e preferências do usuário autenticado.

**TDD**:
- `UserControllerIT.getMeReturnsAuthenticatedUser()`.

**Aceite**:
- Retorna dados do usuário atual.
- Não aceita acesso anônimo.

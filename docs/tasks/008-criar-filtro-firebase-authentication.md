# T0104 — Criar filtro Firebase Authentication

## Épico 1 — Segurança e usuário

**Descrição**: Ler `Authorization: Bearer`, validar token, bootstrapar usuário e popular contexto.

**TDD**:
- `FirebaseAuthenticationFilterTest`: sem header, header inválido, token válido.

**Aceite**:
- Sem token retorna 401 em endpoint protegido.
- Token inválido retorna 401.
- Token válido define principal.

# T0102 — Implementar `FirebaseTokenService`

## Épico 1 — Segurança e usuário

**Descrição**: Validar ID Token via Firebase Admin SDK e mapear dados internos.

**TDD**:
- `FirebaseTokenServiceTest` com mock do Firebase Admin.

**Aceite**:
- Token válido retorna uid/email/name.
- Token inválido lança `UnauthorizedException`.
- Email ausente gera erro controlado.

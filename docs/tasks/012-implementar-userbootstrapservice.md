# T0108 — Implementar `UserBootstrapService`

## Épico 1 — Segurança e usuário

**Descrição**: Buscar ou criar usuário, preferências e categorias especiais no primeiro acesso.

**TDD**:
- `UserBootstrapServiceTest`: novo usuário cria tudo; existente não duplica.

**Aceite**:
- Usuário novo tem currency `BRL`, theme `SYSTEM`, face id false.
- Preferências padrão criadas.
- Categorias especiais criadas.
- Operação idempotente.

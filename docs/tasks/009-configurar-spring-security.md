# T0105 — Configurar Spring Security

## Épico 1 — Segurança e usuário

**Descrição**: Proteger endpoints da API e registrar filtro Firebase.

**TDD**:
- `SecurityIT` com endpoint dummy protegido.

**Aceite**:
- Todos endpoints exigem autenticação, exceto health/docs se criados.
- CSRF desabilitado para API stateless.
- Sessão stateless.

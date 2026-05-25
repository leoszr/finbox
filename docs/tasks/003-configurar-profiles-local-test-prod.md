# T0003 — Configurar profiles local/test/prod

## Épico 0 — Setup do projeto

**Descrição**: Criar configurações por ambiente para datasource, JPA, Flyway e Firebase.

**TDD**:
- `ApplicationContextTest` por profile test.

**Aceite**:
- Profile `test` usa Testcontainers ou datasource isolado.
- Profile `local` aponta para Docker Compose.
- Segredos não ficam hardcoded em `prod`.

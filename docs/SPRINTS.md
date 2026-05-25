# SPRINTS.md — Agrupamento autônomo de tasks

Objetivo: cada sprint contém tasks que podem ser desenvolvidas em sequência, sem precisar de input do usuário, seguindo PRD/PLAN/TASKS existentes.

## Regras de autonomia

- Usar defaults técnicos já documentados em `docs/PLAN.md` e `docs/ARCHITECTURE.md`.
- Não pedir decisão para detalhes internos de implementação, nomes de classes, pacotes, DTOs ou testes.
- Só pedir input se houver conflito explícito entre documentos ou risco destrutivo fora do projeto.
- Ao iniciar sprint, executar tasks na ordem listada.
- Ao concluir task, marcar em `docs/TASKS.md`.
- Ao concluir sprint, rodar testes relevantes e reportar próxima sprint.

## Sprint 0 — Base técnica inicial ✅

Status: concluída.

- [x] T0001 — Criar projeto Spring Boot base
- [x] T0002 — Criar Docker Compose local do PostgreSQL
- [x] T0003 — Configurar profiles local/test/prod
- [x] T0004 — Criar padrão de erros da API

## Sprint 1 — Segurança Firebase e usuário interno ✅

Status: concluída.

Entrega: autenticação Firebase mockável, usuário interno, preferências e endpoints `/me`.

- [x] T0101 — Criar DTO/token interno Firebase
- [x] T0102 — Implementar `FirebaseTokenService`
- [x] T0103 — Criar `AuthenticatedUser`
- [x] T0104 — Criar filtro Firebase Authentication
- [x] T0105 — Configurar Spring Security
- [x] T0106 — Criar migration `users` e `user_preferences`
- [x] T0107 — Criar entities/repositories de usuário
- [x] T0108 — Implementar `UserBootstrapService`
- [x] T0109 — Implementar `GET /me`
- [x] T0110 — Implementar `PATCH /me`
- [x] T0111 — Implementar `PATCH /me/preferences`

## Sprint 2 — Categorias ✅

Status: concluída.

Entrega: domínio de categorias completo, incluindo especiais, normalização, CRUD e isolamento por usuário.

- [x] T0201 — Criar enums de categoria
- [x] T0202 — Criar `TextNormalizer`
- [x] T0203 — Criar migration `categories`
- [x] T0204 — Criar entity/repository de categoria
- [x] T0205 — Implementar criação de categoria
- [x] T0206 — Implementar listagem de categorias
- [x] T0207 — Implementar edição de categoria custom
- [x] T0208 — Implementar exclusão de categoria custom
- [x] T0209 — Criar endpoints CRUD de categorias

## Sprint 3 — Transações básicas e histórico ✅

Status: concluída.

Entrega: transações comuns, histórico paginado/filtrado e endpoints CRUD.

- [x] T0301 — Criar enums de transação e histórico
- [x] T0302 — Criar util de dinheiro
- [x] T0303 — Criar migration `transactions`
- [x] T0304 — Criar entity/repository de transação
- [x] T0305 — Implementar criação de transação comum
- [x] T0306 — Implementar leitura de transação por ID
- [x] T0307 — Implementar edição de transação comum
- [x] T0308 — Implementar exclusão de transação comum
- [x] T0309 — Implementar histórico com filtros e paginação
- [x] T0310 — Criar endpoints CRUD de transações

## Sprint 4 — Budget ✅

Status: concluída.

Entrega: budgets ativos, categorias de budget, ciclos e status de uso.

- [x] T0401 — Criar enums de budget
- [x] T0402 — Criar migrations de budget
- [x] T0403 — Criar entities/repositories de budget
- [x] T0404 — Implementar cálculo de ciclo semanal
- [x] T0405 — Implementar cálculo de ciclo quinzenal
- [x] T0406 — Implementar cálculo de ciclo mensal
- [x] T0408 — Implementar criação de budget
- [x] T0409 — Implementar edição de budget
- [x] T0410 — Implementar cálculo de uso/status do budget
- [x] T0412 — Criar endpoints de budget

## Sprint 5 — Caixas e integração com Guardado ✅

Status: concluída.

Entrega: caixas BRL, caixa padrão Economias, movimentos e integração com transações Guardado.

- [x] T0501 — Criar migration `saving_boxes`
- [x] T0502 — Criar entity/repository de caixas
- [x] T0503 — Implementar criação de caixa
- [x] T0504 — Implementar criação/garantia da caixa “Economias”
- [x] T0505 — Implementar edição de caixa
- [x] T0506 — Implementar exclusão lógica de caixa
- [x] T0507 — Integrar transações “Guardado” com saldo de caixa
- [x] T0508 — Criar endpoints de caixas
- [x] T0509 — Implementar movimentos da caixa

## Sprint 6 — Dashboard ✅

Status: concluída.

Entrega: endpoint de dashboard básico com resumo financeiro.

- [x] T0601 — Implementar dashboard resumo
- [x] T0602 — Criar endpoint `GET /dashboard`

## Sprint 7 — Hardening e deploy base ✅

Status: concluída.

Entrega: índices, testes finais de isolamento, Dockerfile, compose prod e documentação de env vars.

- [x] T0701 — Adicionar índices para consultas frequentes
- [x] T0702 — Adicionar testes de isolamento por usuário
- [x] T0703 — Configurar Dockerfile da API
- [x] T0704 — Criar Docker Compose prod base
- [x] T0705 — Documentar variáveis de ambiente

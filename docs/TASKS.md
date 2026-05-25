# TASKS.md — Tasks atômicas backend FinBox

Tasks separadas em `docs/tasks/`.

Execução por sprint em `docs/SPRINTS.md`.

## Progresso

- Total: 60
- Concluídas: 60
- Pendentes: 0

Marque uma task como concluída trocando `- [ ]` por `- [x]`.


## Épico 0 — Setup do projeto

- [x] [T0001 — Criar projeto Spring Boot base](tasks/001-criar-projeto-spring-boot-base.md)
- [x] [T0002 — Criar Docker Compose local do PostgreSQL](tasks/002-criar-docker-compose-local-do-postgresql.md)
- [x] [T0003 — Configurar profiles local/test/prod](tasks/003-configurar-profiles-local-test-prod.md)
- [x] [T0004 — Criar padrão de erros da API](tasks/004-criar-padrao-de-erros-da-api.md)

## Épico 1 — Segurança e usuário

- [x] [T0101 — Criar DTO/token interno Firebase](tasks/005-criar-dto-token-interno-firebase.md)
- [x] [T0102 — Implementar `FirebaseTokenService`](tasks/006-implementar-firebasetokenservice.md)
- [x] [T0103 — Criar `AuthenticatedUser`](tasks/007-criar-authenticateduser.md)
- [x] [T0104 — Criar filtro Firebase Authentication](tasks/008-criar-filtro-firebase-authentication.md)
- [x] [T0105 — Configurar Spring Security](tasks/009-configurar-spring-security.md)
- [x] [T0106 — Criar migration `users` e `user_preferences`](tasks/010-criar-migration-users-e-user-preferences.md)
- [x] [T0107 — Criar entities/repositories de usuário](tasks/011-criar-entities-repositories-de-usuario.md)
- [x] [T0108 — Implementar `UserBootstrapService`](tasks/012-implementar-userbootstrapservice.md)
- [x] [T0109 — Implementar `GET /me`](tasks/013-implementar-get-me.md)
- [x] [T0110 — Implementar `PATCH /me`](tasks/014-implementar-patch-me.md)
- [x] [T0111 — Implementar `PATCH /me/preferences`](tasks/015-implementar-patch-me-preferences.md)

## Épico 2 — Categorias

- [x] [T0201 — Criar enums de categoria](tasks/016-criar-enums-de-categoria.md)
- [x] [T0202 — Criar `TextNormalizer`](tasks/017-criar-textnormalizer.md)
- [x] [T0203 — Criar migration `categories`](tasks/018-criar-migration-categories.md)
- [x] [T0204 — Criar entity/repository de categoria](tasks/019-criar-entity-repository-de-categoria.md)
- [x] [T0205 — Implementar criação de categoria](tasks/020-implementar-criacao-de-categoria.md)
- [x] [T0206 — Implementar listagem de categorias](tasks/021-implementar-listagem-de-categorias.md)
- [x] [T0207 — Implementar edição de categoria custom](tasks/022-implementar-edicao-de-categoria-custom.md)
- [x] [T0208 — Implementar exclusão de categoria custom](tasks/023-implementar-exclusao-de-categoria-custom.md)
- [x] [T0209 — Criar endpoints CRUD de categorias](tasks/024-criar-endpoints-crud-de-categorias.md)

## Épico 3 — Transações

- [x] [T0301 — Criar enums de transação e histórico](tasks/025-criar-enums-de-transacao-e-historico.md)
- [x] [T0302 — Criar util de dinheiro](tasks/026-criar-util-de-dinheiro.md)
- [x] [T0303 — Criar migration `transactions`](tasks/027-criar-migration-transactions.md)
- [x] [T0304 — Criar entity/repository de transação](tasks/028-criar-entity-repository-de-transacao.md)
- [x] [T0305 — Implementar criação de transação comum](tasks/029-implementar-criacao-de-transacao-comum.md)
- [x] [T0306 — Implementar leitura de transação por ID](tasks/030-implementar-leitura-de-transacao-por-id.md)
- [x] [T0307 — Implementar edição de transação comum](tasks/031-implementar-edicao-de-transacao-comum.md)
- [x] [T0308 — Implementar exclusão de transação comum](tasks/032-implementar-exclusao-de-transacao-comum.md)
- [x] [T0309 — Implementar histórico com filtros e paginação](tasks/033-implementar-historico-com-filtros-e-paginacao.md)
- [x] [T0310 — Criar endpoints CRUD de transações](tasks/034-criar-endpoints-crud-de-transacoes.md)

## Épico 4 — Budget

- [x] [T0401 — Criar enums de budget](tasks/035-criar-enums-de-budget.md)
- [x] [T0402 — Criar migrations de budget](tasks/036-criar-migrations-de-budget.md)
- [x] [T0403 — Criar entities/repositories de budget](tasks/037-criar-entities-repositories-de-budget.md)
- [x] [T0404 — Implementar cálculo de ciclo semanal](tasks/038-implementar-calculo-de-ciclo-semanal.md)
- [x] [T0405 — Implementar cálculo de ciclo quinzenal](tasks/039-implementar-calculo-de-ciclo-quinzenal.md)
- [x] [T0406 — Implementar cálculo de ciclo mensal](tasks/040-implementar-calculo-de-ciclo-mensal.md)
- [x] [T0408 — Implementar criação de budget](tasks/041-implementar-criacao-de-budget.md)
- [x] [T0409 — Implementar edição de budget](tasks/042-implementar-edicao-de-budget.md)
- [x] [T0410 — Implementar cálculo de uso/status do budget](tasks/043-implementar-calculo-de-uso-status-do-budget.md)
- [x] [T0412 — Criar endpoints de budget](tasks/044-criar-endpoints-de-budget.md)

## Épico 5 — Caixas

- [x] [T0501 — Criar migration `saving_boxes`](tasks/045-criar-migration-saving-boxes.md)
- [x] [T0502 — Criar entity/repository de caixas](tasks/046-criar-entity-repository-de-caixas.md)
- [x] [T0503 — Implementar criação de caixa](tasks/047-implementar-criacao-de-caixa.md)
- [x] [T0504 — Implementar criação/garantia da caixa “Economias”](tasks/048-implementar-criacao-garantia-da-caixa-economias.md)
- [x] [T0505 — Implementar edição de caixa](tasks/049-implementar-edicao-de-caixa.md)
- [x] [T0506 — Implementar exclusão lógica de caixa](tasks/050-implementar-exclusao-logica-de-caixa.md)
- [x] [T0507 — Integrar transações “Guardado” com saldo de caixa](tasks/051-integrar-transacoes-guardado-com-saldo-de-caixa.md)
- [x] [T0508 — Criar endpoints de caixas](tasks/052-criar-endpoints-de-caixas.md)
- [x] [T0509 — Implementar movimentos da caixa](tasks/053-implementar-movimentos-da-caixa.md)

## Épico 6 — Dashboard básico

- [x] [T0601 — Implementar dashboard resumo](tasks/054-implementar-dashboard-resumo.md)
- [x] [T0602 — Criar endpoint `GET /dashboard`](tasks/055-criar-endpoint-get-dashboard.md)

## Épico 7 — Qualidade, hardening e deploy futuro

- [x] [T0701 — Adicionar índices para consultas frequentes](tasks/056-adicionar-indices-para-consultas-frequentes.md)
- [x] [T0702 — Adicionar testes de isolamento por usuário](tasks/057-adicionar-testes-de-isolamento-por-usuario.md)
- [x] [T0703 — Configurar Dockerfile da API](tasks/058-configurar-dockerfile-da-api.md)
- [x] [T0704 — Criar Docker Compose prod base](tasks/059-criar-docker-compose-prod-base.md)
- [x] [T0705 — Documentar variáveis de ambiente](tasks/060-documentar-variaveis-de-ambiente.md)

## Checklist global por task de feature

Antes de marcar qualquer task como concluída:

- [ ] Teste unitário criado primeiro e passando.
- [ ] Implementação mínima feita.
- [ ] Teste de integração quando envolve banco/HTTP/security.
- [ ] Migration aplicada quando envolve schema.
- [ ] DTO com Bean Validation.
- [ ] Service contém regra de negócio.
- [ ] Repository filtra por `user_id`.
- [ ] Erro padronizado.
- [ ] Isolamento multiusuário testado.

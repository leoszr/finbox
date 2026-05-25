# PLAN.md — Plano de desenvolvimento backend FinBox

## 1. Objetivo

Construir API REST do FinBox em Java 25 + Spring Boot, com autenticação Firebase Auth, PostgreSQL próprio, Flyway, isolamento estrito por usuário e regras financeiras centralizadas em services.

## 2. Princípios de implementação

- TDD primeiro: escrever teste que falha antes de implementar cada regra.
- Monólito modular por domínio.
- Regra de negócio só em `service`.
- Controllers finos: DTO + validação + chamada ao service.
- Repositories sempre filtram por `user_id` quando recurso pertence a usuário.
- Nunca usar `findById(id)` em recurso multiusuário.
- Toda tabela principal contém `user_id`.
- Toda feature pronta deve ter: migration, validação, teste unitário, teste de integração para fluxo crítico, erro padronizado.

## 3. Arquitetura alvo

```text
src/main/java/leoszr/finance_app
├── config
├── security
├── common
├── user
├── category
├── transaction
├── budget
├── savingbox
└── dashboard
```

## 4. Stack

- Java 25
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL 16
- Flyway
- Bean Validation
- Firebase Admin SDK
- JUnit 5
- AssertJ
- Mockito
- Spring Boot Test
- Spring Security Test
- MockMvc
- Testcontainers
- Maven Surefire/Failsafe
- Docker Compose

## 5. Estratégia TDD

Suite padrão de testes:

- JUnit 5 como framework principal.
- AssertJ para assertions legíveis.
- Mockito para unit tests de services/adapters.
- Spring Boot Test para testes de contexto e integração.
- MockMvc para testes HTTP/controllers Spring MVC.
- Spring Security Test para cenários de autenticação/autorização.
- Testcontainers PostgreSQL para migrations, repositories e fluxos críticos reais.
- Maven Surefire roda `*Test`; Maven Failsafe roda `*IT`.

Convenção de nomes:

```text
src/test/java/.../*Test.java  -> unit/controller slice sem Docker
src/test/java/.../*IT.java    -> integração com Spring/Testcontainers
```

Para cada task:

1. Criar teste unitário do service ou util.
2. Rodar teste e confirmar falha.
3. Implementar menor código possível.
4. Refatorar mantendo verde.
5. Criar/ajustar teste de integração quando endpoint, banco ou segurança estiverem envolvidos.
6. Validar migration com Testcontainers.

Pirâmide de testes:

- Unitários: regras de negócio, cálculo de ciclos, normalização, validação de saldo.
- Integração: repositories, migrations, isolamento por usuário, transações com lock, endpoints críticos.
- Controller/Web: validação DTO, HTTP status, formato de erro, autenticação.

## 6. Ordem macro de entrega

### Fase 1 — Base e autenticação

Entregar projeto Spring Boot, Docker Compose local, Flyway, segurança Firebase, usuário interno, preferências e bootstrap de categorias especiais.

Risco principal: acoplamento forte ao Firebase real nos testes.
Mitigação: interface `FirebaseTokenService`, implementação real separada, mock/stub em testes.

### Fase 2 — Categorias

Entregar CRUD de categorias, normalização, unicidade por usuário, proteção de categorias especiais e migração de transações ao excluir categoria.

### Fase 3 — Transações

Entregar CRUD de receitas/despesas, histórico com filtros/paginação, categoria fallback “Não categorizado” e regra inicial de categoria “Guardado”.

### Fase 4 — Budget simples

Entregar budget ativo, budget por categoria, ciclos semanal/quinzenal/mensal e alertas/status.

### Fase 5 — Caixas simples

Entregar caixas de dinheiro guardado em BRL, caixa padrão “Economias”, metas simples, exclusão lógica e saldo não negativo.

### Fase 6 — Dashboard básico

Entregar dashboard básico com resumo do ciclo, barra de budget, últimas transações e total guardado.

## 7. Modelo de dados e migrations

Criar migrations Flyway incrementais:

1. `V1__create_users_and_preferences.sql`
2. `V2__create_categories.sql`
3. `V3__create_transactions.sql`
4. `V4__create_budgets.sql`
5. `V5__create_budget_categories.sql`
6. `V6__create_saving_boxes.sql`
7. `V7__add_indexes_for_dashboard.sql`

Constraints obrigatórias:

- `users.firebase_uid` único.
- `categories(user_id, normalized_name)` único.
- `budgets(user_id)` único parcial quando `active = true`.
- `budget_categories(budget_id, category_id)` único.
- `saving_boxes(user_id, normalized_name)` único parcial quando ativa.
- `saving_boxes(user_id)` único parcial para default ativa.

## 8. Segurança

Fluxo:

1. App envia `Authorization: Bearer <firebase_id_token>`.
2. Filtro Spring Security valida token.
3. `FirebaseTokenService` extrai `firebase_uid`, email, nome.
4. `UserBootstrapService` busca/cria usuário interno.
5. `AuthenticatedUser` entra no contexto de segurança.
6. Services recebem `userId` via `CurrentUserProvider` ou argumento explícito.

Regras:

- Sem token: 401.
- Token inválido: 401.
- Recurso inexistente para usuário atual: 404, não 403, para evitar enumeração.
- Operação cruzada detectada: 404 ou 403 conforme contexto; padrão recomendado: 404 para recurso por ID.

## 9. Tratamento de erro

Formato único:

```json
{
  "timestamp": "2026-05-24T21:00:00Z",
  "status": 400,
  "error": "BUSINESS_RULE_ERROR",
  "message": "Valor inválido.",
  "path": "/transactions"
}
```

Tipos:

- `VALIDATION_ERROR`
- `NOT_FOUND`
- `FORBIDDEN`
- `BUSINESS_RULE_ERROR`
- `UNAUTHORIZED`
- `INTERNAL_ERROR`

## 10. Contratos principais de API

### Usuário

- `GET /me`
- `PATCH /me`
- `DELETE /me`
- `PATCH /me/preferences`

### Categorias

- `GET /categories`
- `POST /categories`
- `PATCH /categories/{id}`
- `DELETE /categories/{id}`

### Transações

- `GET /transactions`
- `POST /transactions`
- `GET /transactions/{id}`
- `PATCH /transactions/{id}`
- `DELETE /transactions/{id}`

### Budget

- `GET /budget`
- `POST /budget`
- `PATCH /budget`
- `GET /budget/current-cycle`

### Caixas

- `GET /boxes`
- `POST /boxes`
- `GET /boxes/{id}`
- `PATCH /boxes/{id}`
- `DELETE /boxes/{id}`
- `GET /boxes/{id}/movements`

### Dashboard

- `GET /dashboard`

## 11. Plano de testes unitários

### Common/util

- `TextNormalizerTest`: acentos, case, espaços extras.
- `MoneyUtilsTest`: valida mínimo, arredondamento/escala, rejeita negativo.
- `DateUtilsTest`: ranges inclusivos e validação `start <= end`.

### Security/User

- `FirebaseTokenServiceTest`: token válido, token inválido, campos ausentes.
- `UserBootstrapServiceTest`: cria usuário novo, não duplica existente, cria preferências, cria categorias especiais.
- `CurrentUserProviderTest`: usuário autenticado presente, ausente gera erro.

### Category

- `CategoryServiceTest`: cria, atualiza, lista, impede duplicado normalizado, impede renomear/deletar especial, move transações ao deletar.

### Transaction

- `TransactionServiceTest`: cria receita/despesa, rejeita zero/negativo, usa “Não categorizado”, exige caixa em “Guardado”, impede caixa negativa, ajusta saldo ao criar/editar/excluir.

### Budget

- `BudgetCycleServiceTest`: semanal, quinzenal, mensal, datas inclusivas.
- `BudgetServiceTest`: budget ativo único, soma por categoria <= total, calcula uso só com despesas, status normal/warning/exceeded.

### SavingBox

- `SavingBoxServiceTest`: cria caixa em BRL, cria default, impede nome duplicado, impede alterar moeda, impede deletar/renomear default, impede meta em default, soft delete.

### Dashboard

- `DashboardServiceTest`: calcula resumo do ciclo, barra budget, últimas 5 transações e total guardado.

## 12. Plano de testes de integração

- `MigrationIT`: sobe PostgreSQL Testcontainers e roda Flyway completo.
- `SecurityIT`: endpoint protegido sem token retorna 401; token mock válido acessa.
- `UserBootstrapIT`: primeiro acesso cria usuário, preferências e categorias especiais.
- `CategoryIT`: CRUD + isolamento por usuário + constraints reais.
- `TransactionIT`: CRUD + filtros + paginação + guardado/caixa com transação real.
- `BudgetIT`: budget ativo único + cálculo de uso via banco.
- `SavingBoxIT`: constraints parciais, soft delete, default em BRL.
- `DashboardIT`: agregações básicas do dashboard.

## 13. Definição de pronto

Feature pronta quando:

- testes unitários relevantes existem e passam;
- teste de integração do fluxo crítico existe e passa;
- migration criada e validada;
- endpoint documentado por DTO/contrato;
- validação Bean Validation aplicada;
- erro padronizado;
- isolamento por `user_id` comprovado;
- regra em service, não controller;
- sem acesso cruzado entre usuários;
- sem busca direta por ID em recurso multiusuário.

## 14. Marcos de entrega

### Marco 1 — Núcleo financeiro básico

- Firebase Auth validado.
- Usuário interno criado automaticamente.
- Preferências criadas automaticamente.
- Categorias especiais criadas automaticamente.
- CRUD de categorias.
- CRUD de transações.
- Histórico paginado.
- Isolamento por usuário.

### Marco 2 — MVP financeiro validável

- Budget total ativo.
- Budget por categoria.
- Ciclos semanal/quinzenal/mensal e alertas.
- Caixas simples em BRL.
- Caixa default “Economias”.
- Transações “Guardado”.
- Dashboard básico.

### Pós-MVP

- Snapshots técnicos.
- Transferências entre caixas.
- Relatórios.
- Sync offline.
- Multi-moeda.
- Ciclo custom.

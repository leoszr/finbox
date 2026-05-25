# Arquitetura do backend FinBox

## Estado atual

O código executável está em `finance-app/`.

```text
finance-app/
├── pom.xml
├── src/main/java/leoszr/finance_app/FinanceAppApplication.java
├── src/main/resources/application.properties
└── src/test/java/leoszr/finance_app/
    ├── FinanceAppApplicationTests.java
    ├── TestcontainersConfiguration.java
    └── TestFinanceAppApplication.java
```

Hoje o projeto é um esqueleto Spring Boot gerado pelo Initializr. Ainda não há módulos de domínio, controllers, services, repositories, migrations ou configuração real de segurança.

## Stack configurada no `pom.xml`

- Spring Boot `4.0.6`
- Java configurado como `25`
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Bean Validation
- Flyway
- PostgreSQL driver
- Lombok
- JUnit 5 via starters de teste
- Testcontainers com PostgreSQL

## Arquitetura alvo

Arquitetura planejada: monólito modular por domínio, com regras de negócio em services e controllers finos.

Pacote base esperado pelo plano/tasks:

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

Responsabilidades:

- `config`: beans, properties, CORS, OpenAPI futuro, config geral.
- `security`: filtro Firebase, autenticação Spring Security, usuário autenticado, provedor de usuário atual.
- `common`: erros padronizados, exceções, utilitários compartilhados, normalização, dinheiro/data.
- `user`: usuário interno, preferências, bootstrap no primeiro acesso, endpoints `/me`.
- `category`: categorias especiais/custom, normalização, CRUD isolado por usuário.
- `transaction`: receitas/despesas/guardado, histórico, paginação, regras de saldo.
- `budget`: budget ativo, categorias do budget, ciclos semanal/quinzenal/mensal, status de uso.
- `savingbox`: caixas de economia, caixa padrão, saldo, movimentos e soft delete.
- `dashboard`: agregações de resumo financeiro do usuário.

## Camadas por domínio

Cada domínio deve seguir padrão:

```text
<domain>/
├── controller
├── dto
├── entity
├── repository
└── service
```

Regras:

- Controller recebe HTTP, valida DTO e chama service.
- Service contém regra de negócio e transação.
- Repository encapsula consultas JPA.
- Recurso multiusuário nunca usa `findById(id)` direto; consultas filtram por `user_id`.
- DTOs expõem contrato HTTP; entities ficam internas.

## Fluxo de autenticação alvo

```text
Cliente
  -> Authorization: Bearer <firebase_id_token>
  -> FirebaseAuthenticationFilter
  -> FirebaseTokenService
  -> UserBootstrapService
  -> SecurityContext com AuthenticatedUser
  -> Controller
  -> Service com userId atual
  -> Repository filtrando por user_id
```

Sem token ou token inválido deve retornar `401`. Recurso de outro usuário deve parecer inexistente (`404`) quando buscado por ID.

## Banco e migrations alvo

Flyway deve versionar schema em `src/main/resources/db/migration`:

1. `V1__create_users_and_preferences.sql`
2. `V2__create_categories.sql`
3. `V3__create_transactions.sql`
4. `V4__create_budgets.sql`
5. `V5__create_budget_categories.sql`
6. `V6__create_saving_boxes.sql`
7. `V7__add_indexes_for_dashboard.sql`

Tabelas principais devem ter `user_id`. Constraints importantes:

- `users.firebase_uid` único.
- `categories(user_id, normalized_name)` único.
- Budget ativo único por usuário.
- Caixa padrão ativa única por usuário.

## Testes

Estratégia alvo:

- JUnit 5 como framework principal.
- AssertJ para assertions legíveis.
- Mockito para unit tests de services/adapters.
- Spring Boot Test para contexto e integração.
- MockMvc para testes HTTP/controllers Spring MVC.
- Spring Security Test para autenticação/autorização.
- Testcontainers PostgreSQL para migrations, repositories e fluxos críticos reais.
- Maven Surefire roda `*Test`; Maven Failsafe roda `*IT`.

Convenção:

```text
src/test/java/.../*Test.java  -> unit/controller slice sem Docker
src/test/java/.../*IT.java    -> integração com Spring/Testcontainers
```

Teste atual:

- `FinanceAppApplicationTests.contextLoads()` carrega contexto Spring com PostgreSQL Testcontainers.
- Validado com Java 25 via `mise exec -- ./mvnw test`: `BUILD SUCCESS`.

## Gaps arquiteturais atuais

- Não há árvore modular `config/security/common/user/...`.
- Não há migrations Flyway ainda.
- Ambiente shell padrão pode apontar para Java diferente; use `mise exec -- ...` ou ative `mise` no shell para garantir Java 25.

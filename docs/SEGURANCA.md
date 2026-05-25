# SEGURANCA.md — Achados de segurança

Registro vivo de riscos encontrados ao fim de cada sprint.

## Sprint 2 — Categorias

### S2-001 — Autenticação mock ativa globalmente

**Severidade:** crítica

**Status:** corrigido. `MockAuthTokenService` agora carrega só com profiles `local` e `test`.

**Problema:** `MockFirebaseTokenService` estava anotado com `@Service` sem `@Profile`. Se aplicação subisse em produção assim, qualquer cliente poderia autenticar usando um token textual no formato `uid|email|name`.

**Impacto:** invasor consegue criar identidade arbitrária, acessar endpoints autenticados e criar dados como qualquer usuário fabricado.

**Solução:**
- Restringido mock aos profiles `local` e `test` com `@Profile({"local", "test"})`.
- Criado `SupabaseAuthTokenService` para profile `supabase`, validando JWT via JWKS.
- Produção deve usar `SPRING_PROFILES_ACTIVE=prod,supabase` e `SUPABASE_PROJECT_REF`.
- Pendente opcional: teste específico garantindo que profile `prod` não carrega mock.

### S2-002 — Erros inesperados no filtro de autenticação podem virar 500

**Severidade:** média

**Status:** corrigido. `BearerAuthenticationFilter` captura falhas inesperadas e retorna `401` genérico.

**Problema:** `FirebaseAuthenticationFilter` capturava só `UnauthorizedException`. Token malformado ou erro inesperado no parsing/validação podia escapar como erro interno.

**Impacto:** resposta 500 expõe comportamento interno, polui logs e pode facilitar enumeração/DoS simples.

**Solução:**
- Capturar exceções de validação/token no filtro e retornar sempre `401` com mensagem genérica.
- Logar detalhes internamente sem enviar ao cliente.
- Limpar `SecurityContextHolder` em qualquer falha.
- Adicionar teste com token malformado retornando `401`, não `500`.

### S2-003 — Race condition em criação/edição de categoria duplicada

**Severidade:** média

**Problema:** `CategoryService` verifica duplicidade antes de salvar. Duas requisições concorrentes podem passar no check e uma bater na constraint única do banco.

**Impacto:** constraint protege integridade, mas erro pode retornar `500` em vez de erro padronizado.

**Solução:**
- Manter constraint `uk_category_name_per_user`.
- Capturar `DataIntegrityViolationException` em criação/edição e converter para `BusinessRuleException("Categoria já existe.")`.
- Adicionar teste de integração validando retorno `400 BUSINESS_RULE_ERROR` para duplicidade real.

### S2-004 — Sem rate limiting

**Severidade:** média/baixa no MVP; média em produção

**Problema:** API não limita volume de requisições por IP/usuário/token.

**Impacto:** facilita brute force de tokens inválidos, abuso de endpoints e DoS de baixo custo.

**Solução:**
- Aplicar rate limit no gateway/reverse proxy em produção.
- Limitar endpoints autenticados por usuário e IP.
- Limitar endpoints anônimos como `/actuator/health` por IP.
- Registrar métricas de `401`, `403`, `429`.

## Sprint 3 — Transações

### S3-001 — `box_id` não existia no DTO inicial de transação

**Severidade:** baixa/média

**Problema:** Sprint 3 criou `box_id` opcional no banco, mas o contrato inicial de transação não recebia `boxId`. A regra de categoria `Guardado` só ficou completa na Sprint 5.

**Impacto:** antes da integração com caixas, transações `Guardado` eram bloqueadas por regra de negócio, evitando saldo incorreto. Risco funcional, não vazamento direto.

**Solução:** corrigido na Sprint 5 com `boxId` no `TransactionRequest` e validação por `user_id` + lock pessimista.

### S3-002 — Busca textual usa `LIKE` sem limite específico

**Severidade:** baixa

**Problema:** filtro `description` em histórico usa `LIKE '%texto%'`. Em base grande, consultas podem ficar caras.

**Impacto:** usuário autenticado pode causar carga elevada no próprio histórico; risco de DoS leve se combinado com alto volume.

**Solução:** manter paginação e `size <= 100`; adicionar índice/trigram ou busca normalizada se histórico crescer; aplicar rate limit em produção.

## Sprint 4 — Budget

### S4-001 — Uso do budget soma todas despesas, não apenas categorias configuradas

**Severidade:** baixa

**Problema:** `BudgetService.currentCycle()` soma todas despesas do usuário no ciclo. Se budget por categoria for interpretado como limite segmentado, pode divergir do esperado.

**Impacto:** risco de cálculo incorreto exibido ao usuário, sem impacto de isolamento ou acesso indevido.

**Solução:** documentar regra como budget total global ou implementar soma por categorias configuradas quando endpoint detalhado existir.

### S4-002 — Race condition em budget ativo único

**Severidade:** média

**Problema:** `BudgetService.create()` verifica `existsByUserIdAndActiveTrue` antes de salvar. Requisições concorrentes podem passar no check e uma bater no índice parcial `uk_active_budget_per_user`.

**Impacto:** integridade protegida pelo banco, mas erro pode virar `500` em vez de erro padronizado.

**Solução:** capturar `DataIntegrityViolationException` e retornar `BUSINESS_RULE_ERROR`; adicionar teste concorrente/constraint real.

## Sprint 5 — Caixas e Guardado

### S5-001 — Race condition em nome/default de caixa

**Severidade:** média

**Problema:** criação de caixa e `ensureDefault()` fazem check/busca antes de salvar. Concorrência pode bater nos índices parciais `uk_active_saving_box_name_per_user` e `uk_active_default_saving_box_per_user`.

**Impacto:** banco preserva integridade, mas API pode responder `500` em corrida.

**Solução:** capturar `DataIntegrityViolationException` e converter para erro padronizado; em `ensureDefault()`, ao conflito, reler default ativo.

### S5-002 — Validação de saldo depende de lock correto

**Severidade:** média

**Problema:** transações `Guardado` atualizam saldo com `@Lock(PESSIMISTIC_WRITE)`. Isso é correto, mas depende de todas as futuras alterações de saldo usarem o mesmo caminho.

**Impacto:** qualquer alteração futura sem lock pode permitir saldo inconsistente ou negativo por corrida.

**Solução:** manter saldo mutável apenas via service transacional central; proibir updates diretos; adicionar testes concorrentes de saldo.

### S5-003 — `box_id` só é UUID no DTO, sem Bean Validation contextual

**Severidade:** baixa

**Problema:** regra `Guardado exige boxId` e `categoria comum não aceita caixa` fica no service, não no DTO.

**Impacto:** seguro, pois service valida e filtra por `user_id`; risco apenas de mensagens de validação menos específicas.

**Solução:** manter regra no service; opcionalmente adicionar testes HTTP específicos para erros de `boxId`.

## Checklist obrigatório ao fim de cada sprint

- Rodar testes unitários e integração relevantes.
- Revisar autenticação/autorização dos endpoints novos.
- Confirmar isolamento por `user_id` em repositories/services.
- Verificar DTOs para evitar vazamento de entity/campos internos.
- Verificar validação de input e tratamento padronizado de erro.
- Verificar migrations/constraints contra bypass concorrente.
- Registrar achados neste arquivo.

# WTC Challenge — Backend

API REST desenvolvida em **Java Spring Boot** para gerenciamento de comunicação e CRM corporativo da WTC (World Trade Center). Projeto entregue como **Sprint 2** do Challenge FIAP.

---

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3 |
| Segurança | Spring Security + JWT (JJWT 0.12) |
| Banco de Dados | **MongoDB Atlas** (NoSQL — nuvem) |
| ODM | Spring Data MongoDB |
| Push Notifications | Firebase Admin SDK (FCM) |
| Documentação | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven 3.9 |

---

## Arquitetura

```
src/main/java/com/wtc/
├── audit/          # Aspecto de auditoria (@Auditable)
├── config/         # Configurações (Security, CORS, Jackson)
├── controller/     # Endpoints REST
├── dto/
│   ├── request/    # Payloads de entrada
│   └── response/   # Payloads de saída
├── entity/         # Entidades JPA
├── enums/          # Enumerações de domínio
├── exception/      # GlobalExceptionHandler
├── firebase/       # Integração Firebase (push)
├── repository/     # Repositórios Spring Data
├── security/       # JWT Filter e utilitários
└── service/        # Regras de negócio
```

---

## Configuração

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Oracle Database (ou acesso ao Oracle FIAP)
- Conta Firebase com projeto configurado

### Banco de Dados — MongoDB Atlas

O projeto usa **MongoDB Atlas** (free tier). A connection string já está configurada em `application.properties`.

Para usar seu próprio cluster, altere:
```properties
spring.data.mongodb.uri=mongodb+srv://USER:PASS@cluster.mongodb.net/wtc?appName=M0Free
spring.data.mongodb.database=wtc
```

### Firebase

Baixe a chave de serviço do Firebase Console:
> Configurações do projeto → Contas de serviço → Gerar nova chave privada

Salve como `src/main/resources/firebase-service-account.json`.

### Banco de Dados

MongoDB Atlas — sem scripts necessários. As coleções são criadas automaticamente ao rodar a aplicação.

Collections criadas automaticamente:
- `users` · `clients` · `segments` · `messages`
- `campaigns` · `campaign_recipients` · `annotations`
- `audit_logs` · `tasks`

---

## Como executar

```bash
# Clonar e entrar na pasta
git clone <repo-url>
cd wtc-backend

# Rodar
mvn spring-boot:run
```

O servidor sobe em `http://localhost:8080`.

**Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Endpoints da API

### Autenticação
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/login` | Login de operador ou cliente |
| POST | `/api/auth/register` | Cadastro de usuário |
| PUT | `/api/auth/fcm-token` | Atualizar token FCM do dispositivo |

### CRM — Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/crm/clients` | Listar com filtros (status, segmento, tag) |
| GET | `/api/crm/clients/search?q=` | Busca por nome, e-mail ou empresa |
| GET | `/api/crm/clients/{id}` | Buscar por ID |
| GET | `/api/crm/clients/{id}/profile360` | Perfil 360°: mensagens, campanhas, tarefas, anotações |
| POST | `/api/crm/clients` | Cadastrar novo cliente |
| PUT | `/api/crm/clients/{id}` | Atualizar dados |
| POST | `/api/crm/clients/{id}/annotations` | Adicionar anotação |

### Segmentos
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/segments` | Listar todos |
| GET | `/api/segments/{id}` | Buscar por ID |
| POST | `/api/segments` | Criar segmento |
| PUT | `/api/segments/{id}` | Atualizar |
| DELETE | `/api/segments/{id}` | Excluir |

### Chat & Mensageria
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/messages` | Enviar mensagem (1:1 ou por segmento) |
| GET | `/api/messages/conversation/{userId}` | Histórico de conversa |
| PATCH | `/api/messages/{id}/status` | Atualizar status (SENT/DELIVERED/READ/FAILED) |

### Campanhas Express
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/campaigns` | Listar campanhas |
| GET | `/api/campaigns/{id}` | Buscar por ID |
| POST | `/api/campaigns` | Criar campanha |
| POST | `/api/campaigns/{id}/send` | Enviar imediatamente para o segmento via FCM |

### Auditoria
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/audit` | Listar logs de auditoria |

---

## Modelo de Dados

```
WTC_USERS          → Operadores e clientes do sistema
WTC_SEGMENTS       → Agrupamentos de clientes
WTC_CLIENTS        → Perfis CRM com dados empresariais
WTC_MESSAGES       → Mensagens de chat
WTC_CAMPAIGNS      → Campanhas de push notification
WTC_CAMPAIGN_RECIPIENTS → Destinatários de campanhas
WTC_ANNOTATIONS    → Anotações de operadores sobre clientes
WTC_TASKS          → Tarefas vinculadas a atendimentos
WTC_AUDIT_LOGS     → Registro de auditoria das operações
```

### Campos empresariais do Cliente
- `sector`: FINANCEIRO | INDUSTRIAL | COMERCIO | TECNOLOGIA | SAUDE | EDUCACAO | SERVICOS | AGRONEGOCIO | OUTROS
- `companyLevel`: STARTUP | PEQUENA | MEDIA | GRANDE | ENTERPRISE
- `employeeCount`: número de funcionários
- `score`: pontuação de 0 a 100
- `status`: ACTIVE | INACTIVE | BLOCKED | PROSPECT
- `tags`: labels separadas por vírgula

---

## Segurança

- **JWT** com expiração de 24h
- **Roles**: `OPERATOR` (acesso total ao CRM, campanhas, segmentos) e `CLIENT` (apenas chat)
- **CORS** configurado para permitir o app Flutter
- **Audit logs** automáticos via AOP (`@Auditable`)

---

## Diferencial Implementado

**Integração Firebase Cloud Messaging (FCM)** — ao enviar uma mensagem ou campanha, o backend dispara push notifications em tempo real para os dispositivos dos clientes via Firebase Admin SDK.

---

## Equipe

Desenvolvido para o Challenge WTC — FIAP 2026.

# RafaLink CRM

> **Inteligência em Relacionamentos e Vendas**
> FIAP — Engenharia de Software · 4° Semestre · Domain Driven Design — Java/Spring

**Integrantes:**

| Nome | RM       |
|---|----------|
| Felipe Silva do Prado Lima | RM559848 |
| Luís Felipe Crivellaro | RM560877 |
| Rafael Mandel | RM560333 |

---

## Estrutura do projeto

```
src/main/java/br/com/rafalink/crm/
├── application/
│   └── service/                        ← Regras de negócio e casos de uso
│       ├── AgendamentoService.java
│       ├── CampanhaService.java
│       ├── LeadService.java
│       └── UsuarioService.java
│
├── domain/
│   ├── exception/                      ← Exceções de domínio customizadas
│   │   ├── BusinessException.java
│   │   ├── InvalidStatusException.java
│   │   └── ResourceNotFoundException.java
│   ├── model/                          ← Entidades JPA e enums
│   │   ├── Agendamento.java
│   │   ├── CanalCampanha.java
│   │   ├── Campanha.java
│   │   ├── Lead.java
│   │   ├── PerfilUsuario.java
│   │   ├── StatusAgendamento.java
│   │   ├── StatusLead.java
│   │   └── Usuario.java
│   └── repository/                     ← Interfaces JPA
│       ├── AgendamentoRepository.java
│       ├── CampanhaRepository.java
│       ├── LeadRepository.java
│       └── UsuarioRepository.java
│
└── exposition/
    ├── controller/
    │   ├── json/                       ← @RestController — saída JSON (/api/**)
    │   │   ├── AgendamentoController.java
    │   │   ├── CampanhaController.java
    │   │   ├── LeadController.java
    │   │   └── UsuarioController.java
    │   └── view/                       ← @Controller — páginas Thymeleaf (/view/**)
    │       ├── AgendamentoViewController.java
    │       ├── CampanhaViewController.java
    │       ├── HomeViewController.java
    │       ├── LeadViewController.java
    │       └── UsuarioViewController.java
    ├── dto/                            ← Records de entrada (Request) e saída (Response)
    ├── handler/
    │   ├── GlobalExceptionHandler.java ← Trata exceções dos JSONControllers → JSON
    │   └── ViewExceptionHandler.java   ← Trata exceções dos ViewControllers → redirect
    └── ...

src/main/resources/
├── db/migration/                       ← Flyway: V1 a V5
└── templates/
    ├── layout/
    │   └── base.html                   ← Layout base
    ├── home.html                       ← Dashboard com métricas e docs de endpoints
    ├── usuarios/
    │   ├── lista.html
    │   ├── detalhe.html
    │   └── formulario.html
    ├── leads/
    │   ├── lista.html
    │   ├── detalhe.html
    │   └── formulario.html
    ├── agendamentos/
    │   ├── lista.html
    │   ├── detalhe.html
    │   └── formulario.html
    └── campanhas/
        ├── lista.html
        ├── detalhe.html
        └── formulario.html
```

---

## Como executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ rodando localmente

### 1. Criar o banco de dados

```sql
CREATE DATABASE rafalink_crm;
```

### 2. Executar

```bash
./mvnw spring-boot:run
```

O Flyway aplica as migrations automaticamente na primeira execução.

### 3. Acessar

| Interface | URL |
|---|---|
| Dashboard (views) | http://localhost:8080/view |
| API JSON | http://localhost:8080/api |

---

## Rotas das Views (Thymeleaf)

Páginas renderizadas server-side. Cada página exibe também os endpoints JSON equivalentes, servindo como documentação de uso.

### Dashboard
| Método | Rota | Descrição |
|---|---|---|
| GET | `/view` | Dashboard com métricas e documentação de endpoints |
| GET | `/` | Redireciona para `/view` |

### Usuários
| Método | Rota | Descrição |
|---|---|---|
| GET | `/view/usuarios` | Listar todos os usuários |
| GET | `/view/usuarios/{id}` | Detalhe do usuário |
| GET | `/view/usuarios/novo` | Formulário de cadastro |
| POST | `/view/usuarios` | Processar cadastro |
| POST | `/view/usuarios/{id}/desativar` | Desativar usuário |

### Leads
| Método | Rota | Descrição |
|---|---|---|
| GET | `/view/leads` | Listar todos os leads com contadores por status |
| GET | `/view/leads/{id}` | Detalhe do lead |
| GET | `/view/leads/novo` | Formulário de cadastro |
| POST | `/view/leads` | Processar cadastro |
| POST | `/view/leads/{id}/status` | Atualizar status do lead |
| POST | `/view/leads/arquivar-inativos` | Arquivar leads inativos há >90 dias (RN06) |

### Agendamentos
| Método | Rota | Descrição |
|---|---|---|
| GET | `/view/agendamentos` | Listar agendamentos (filtro por usuário opcional) |
| GET | `/view/agendamentos/{id}` | Detalhe do agendamento |
| GET | `/view/agendamentos/novo` | Formulário de criação |
| POST | `/view/agendamentos` | Processar criação |
| POST | `/view/agendamentos/{id}/cancelar` | Cancelar agendamento |

### Campanhas
| Método | Rota | Descrição |
|---|---|---|
| GET | `/view/campanhas` | Listar campanhas ativas |
| GET | `/view/campanhas/todas` | Listar todas as campanhas |
| GET | `/view/campanhas/{id}` | Detalhe da campanha |
| GET | `/view/campanhas/nova` | Formulário de criação |
| POST | `/view/campanhas` | Processar criação |
| POST | `/view/campanhas/{id}/encerrar` | Encerrar campanha |

---

## Endpoints JSON (API REST)

Retornam `application/json`. Consulte o Dashboard em `/view` para documentação visual completa.

### Usuários — `/api/usuarios`

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| POST | `/api/usuarios` | Cadastrar usuário | 201 Created |
| GET | `/api/usuarios` | Listar todos | 200 OK |
| GET | `/api/usuarios/{id}` | Buscar por ID | 200 OK / 404 |
| PATCH | `/api/usuarios/{id}/desativar` | Desativar | 200 OK / 422 |

**Corpo do POST:**
```json
{
  "nome": "João Silva",
  "email": "joao@empresa.com",
  "senha": "senha123",
  "perfil": "ADMINISTRADOR | MARKETING | VENDAS | CLIENTE"
}
```

### Leads — `/api/leads`

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| POST | `/api/leads` | Cadastrar lead | 201 Created |
| GET | `/api/leads` | Listar todos | 200 OK |
| GET | `/api/leads/{id}` | Buscar por ID | 200 OK / 404 |
| GET | `/api/leads/status/{status}` | Filtrar por status | 200 OK |
| PATCH | `/api/leads/{id}/status?novoStatus=` | Atualizar status | 200 OK / 422 |
| PATCH | `/api/leads/arquivar-inativos` | Arquivar inativos (RN06) | 200 OK |

**Status válidos:** `NOVO` · `EM_CONTATO` · `CONVERTIDO` · `PERDIDO`

**Corpo do POST:**
```json
{
  "nome": "Maria Souza",
  "email": "maria@cliente.com",
  "telefone": "11999999999",
  "origem": "Site"
}
```

### Agendamentos — `/api/agendamentos`

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| POST | `/api/agendamentos` | Criar agendamento | 201 Created |
| GET | `/api/agendamentos/{id}` | Buscar por ID | 200 OK / 404 |
| GET | `/api/agendamentos/usuario/{id}` | Listar por usuário | 200 OK |
| GET | `/api/agendamentos/lead/{id}` | Listar por lead | 200 OK |
| PATCH | `/api/agendamentos/{id}/cancelar` | Cancelar | 200 OK / 422 |

**Corpo do POST:**
```json
{
  "titulo": "Reunião de alinhamento",
  "descricao": "Apresentação da proposta comercial",
  "dataHora": "2026-06-15T14:00:00",
  "usuarioId": 1,
  "leadId": 2
}
```

### Campanhas — `/api/campanhas`

| Verbo | Rota | Descrição | Retorno |
|---|---|---|---|
| POST | `/api/campanhas` | Criar campanha | 201 Created |
| GET | `/api/campanhas` | Listar ativas | 200 OK |
| GET | `/api/campanhas/todas` | Listar todas | 200 OK |
| GET | `/api/campanhas/{id}` | Buscar por ID | 200 OK / 404 |
| PATCH | `/api/campanhas/{id}/encerrar` | Encerrar | 200 OK / 422 |

**Canais válidos:** `EMAIL` · `WHATSAPP` · `SMS` · `REDES_SOCIAIS`

**Corpo do POST:**
```json
{
  "nome": "Black Friday 2026",
  "descricao": "Campanha de fim de ano",
  "canal": "EMAIL",
  "inicio": "2026-11-25",
  "fim": "2026-11-30"
}
```

---

## Códigos de retorno

| Código | Significado |
|---|---|
| 200 OK | Operação realizada com sucesso |
| 201 Created | Recurso criado com sucesso |
| 400 Bad Request | Dados inválidos ou status inexistente |
| 404 Not Found | Recurso não encontrado |
| 422 Unprocessable Entity | Regra de negócio violada |
| 500 Internal Server Error | Erro inesperado no servidor |

---

## Regras de negócio implementadas

| Código | Regra |
|---|---|
| RN01 | Somente usuários autenticados acessam o sistema |
| RN02 | Cada lead possui apenas um status por vez |
| RN03 | O sistema não gera dados fictícios |
| RN05 | Administrador, marketing e cliente possuem acessos específicos |
| RN06 | Leads inativos há mais de 90 dias são arquivados automaticamente |
| RN07 | Administradores não podem ser desativados pelo sistema |
| RN08 | Previsões exibem margem de erro e nível de confiança |
| RN10 | Todas as alterações críticas são registradas em log |

---

## Migrations Flyway

| Versão | Arquivo | Descrição |
|---|---|---|
| V1 | `V1__create_usuarios.sql` | Tabela `usuarios` |
| V2 | `V2__create_leads.sql` | Tabela `leads` |
| V3 | `V3__create_agendamentos.sql` | Tabela `agendamentos` |
| V4 | `V4__create_campanhas.sql` | Tabela `campanhas` |
| V5 | `V5__fix_perfil_marketing.sql` | Corrige typo `MARKENTING → MARKETING` |

---

## Stack tecnológica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.5 |
| API REST | Spring Web MVC (`@RestController`) |
| Views | Thymeleaf + Bootstrap 5 |
| Persistência | Spring Data JPA + Hibernate |
| Banco (dev) | PostgreSQL |
| Migrations | Flyway |
| Validação | Bean Validation (Jakarta) |
| Build | Maven |
| Boilerplate | Lombok |

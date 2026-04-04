# RafaLink CRM — Inteligência em Relacionamentos e Vendas

> Projeto acadêmico — FIAP Engenharia de Software 4° Semestre  
> Disciplina: Domain Driven Design — Sprint 3

---

## Integrantes

| Nome | RM |
|---|---|
| Felipe Silva do Prado Lima | RM559848 |
| Luis Felipe Crivallaro | RM560877 |
| Rafael Mandel | RM560333 |

---

## Descrição do Projeto

O **RafaLink CRM** é uma plataforma inteligente de gestão de relacionamento com clientes (CRM), desenvolvida para automatizar processos comerciais, otimizar a captação e conversão de leads, e apoiar a tomada de decisões estratégicas com base em dados. A solução integra as frentes de marketing, vendas e atendimento em um único ecossistema.

---

## Arquitetura de Pacotes

O projeto segue a arquitetura em camadas baseada em **Domain Driven Design (DDD)**:

```
br.com.rafalink.crm
├── domain/
│   ├── model/          ← Entidades e Enums do domínio
│   ├── repository/     ← Interfaces de repositório (Spring Data JPA)
│   └── exception/      ← Exceptions customizadas de negócio
├── application/
│   └── service/        ← Regras de negócio por responsabilidade
└── exposition/
    ├── controller/     ← Endpoints REST (Spring MVC)
    ├── dto/            ← Objetos de transferência de dados
    └── handler/        ← Tratamento global de exceções
```

---

## Entidades de Domínio

### `Usuario`
Representa os usuários do sistema com perfis de acesso: `ADMINISTRADOR`, `MARKETING`, `VENDAS` e `CLIENTE`.

### `Lead`
Representa um potencial cliente no funil de vendas. Possui status controlado: `NOVO`, `EM_CONTATO`, `CONVERTIDO` e `PERDIDO`.

### `Agendamento`
Representa reuniões e compromissos vinculados a leads e usuários responsáveis.

### `Campanha`
Representa campanhas de marketing em diferentes canais: `EMAIL`, `WHATSAPP`, `SMS` e `REDES_SOCIAIS`.

---

## Regras de Negócio Implementadas

| # | Serviço | Regra |
|---|---|---|
| RN1 | UsuarioService | E-mail deve ser único e ter formato válido |
| RN2 | UsuarioService | Administradores não podem ser desativados |
| RN3 | LeadService | Lead exige e-mail válido; status inicial sempre `NOVO` (RN02 do documento) |
| RN4 | LeadService | Status só pode ser alterado se o lead não estiver arquivado |
| RN5 | LeadService | Leads sem atualização há mais de 90 dias são arquivados automaticamente (RN06 do documento) |
| RN6 | AgendamentoService | Agendamento não pode ser criado em data/hora passada |
| RN7 | AgendamentoService | Cancelamento só é permitido para agendamentos com status `AGENDADO` |
| RN8 | CampanhaService | Nome da campanha deve ser único; data de fim não pode ser anterior ao início |
| RN9 | CampanhaService | Encerramento define a data de fim como hoje e desativa a campanha |

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 17 instalado
- PostgreSQL rodando localmente na porta 5432
- Maven instalado (ou usar o wrapper `./mvnw`)

### 1. Clonar o repositório
```bash
git clone https://github.com/rflMandell/rafalink-crm.git
```

### 2. Criar o banco de dados
```bash
psql -U postgres
```
```sql
CREATE DATABASE rafalink_crm;
CREATE USER rafalink_user WITH PASSWORD 'rafalink123';
GRANT ALL PRIVILEGES ON DATABASE rafalink_crm TO rafalink_user;
\q
```

### 3. Configurar o `application.properties`
O arquivo já vem configurado em `src/main/resources/application.properties` com as credenciais acima.

### 4. Rodar a aplicação
```bash
./mvnw spring-boot:run
```

O Flyway executará automaticamente as migrations e criará as tabelas. A API estará disponível em `http://localhost:8080`.

---

## Endpoints da API

### Usuários — `/api/usuarios`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/usuarios` | Cadastrar usuário |
| `GET` | `/api/usuarios` | Listar todos |
| `GET` | `/api/usuarios/{id}` | Buscar por ID |
| `PATCH` | `/api/usuarios/{id}/desativar` | Desativar usuário |

### Leads — `/api/leads`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/leads` | Cadastrar lead |
| `GET` | `/api/leads` | Listar todos |
| `GET` | `/api/leads/{id}` | Buscar por ID |
| `GET` | `/api/leads/status/{status}` | Listar por status |
| `PATCH` | `/api/leads/{id}/status?novoStatus=` | Atualizar status |
| `PATCH` | `/api/leads/arquivar-inativos` | Arquivar leads inativos (RN06) |

### Agendamentos — `/api/agendamentos`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/agendamentos` | Criar agendamento |
| `GET` | `/api/agendamentos/{id}` | Buscar por ID |
| `GET` | `/api/agendamentos/usuario/{id}` | Listar por usuário |
| `PATCH` | `/api/agendamentos/{id}/cancelar` | Cancelar agendamento |

### Campanhas — `/api/campanhas`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/campanhas` | Criar campanha |
| `GET` | `/api/campanhas` | Listar ativas |
| `GET` | `/api/campanhas/todas` | Listar todas |
| `GET` | `/api/campanhas/{id}` | Buscar por ID |
| `PATCH` | `/api/campanhas/{id}/encerrar` | Encerrar campanha |

---

## Migrations Flyway

| Arquivo | Descrição |
|---|---|
| `V1__create_usuarios.sql` | Tabela de usuários |
| `V2__create_leads.sql` | Tabela de leads com FK para usuários |
| `V3__create_agendamentos.sql` | Tabela de agendamentos com FKs |
| `V4__create_campanhas.sql` | Tabela de campanhas |

---

## Exceptions Customizadas

| Exception | HTTP Status | Uso |
|---|---|---|
| `ResourceNotFoundException` | 404 | Recurso não encontrado por ID |
| `BusinessException` | 422 | Violação de regra de negócio |
| `InvalidStatusException` | 400 | Status de lead inválido |

---

## Estrutura de Migrations

```
src/main/resources/
├── application.properties
└── db/migration/
    ├── V1__create_usuarios.sql
    ├── V2__create_leads.sql
    ├── V3__create_agendamentos.sql
    └── V4__create_campanhas.sql
```

---

*FIAP — Faculdade de Informática e Administração Paulista · 2025*
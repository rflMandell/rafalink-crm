CREATE TABLE leads (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    telefone        VARCHAR(20),
    origem          VARCHAR(50),
    status          VARCHAR(20)  NOT NULL DEFAULT 'NOVO',
    responsavel_id  BIGINT REFERENCES usuarios(id),
    criado_em       TIMESTAMP    NOT NULL DEFAULT NOW(),
    atualizado_em   TIMESTAMP    NOT NULL DEFAULT NOW(),
    arquivado       BOOLEAN      NOT NULL DEFAULT FALSE
);
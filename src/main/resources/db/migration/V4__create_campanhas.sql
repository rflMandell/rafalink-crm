CREATE TABLE campanhas (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    descricao    TEXT,
    canal        VARCHAR(50)  NOT NULL,
    inicio       DATE         NOT NULL,
    fim          DATE,
    ativa        BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em    TIMESTAMP    NOT NULL DEFAULT NOW()
);
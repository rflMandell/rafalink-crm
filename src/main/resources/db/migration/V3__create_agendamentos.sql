CREATE TABLE agendamentos (
    id          BIGSERIAL PRIMARY KEY,
    titulo      VARCHAR(150) NOT NULL,
    descricao   TEXT,
    data_hora   TIMESTAMP    NOT NULL,
    lead_id     BIGINT REFERENCES leads(id),
    usuario_id  BIGINT REFERENCES usuarios(id),
    status      VARCHAR(20)  NOT NULL DEFAULT 'AGENDADO',
    criado_em   TIMESTAMP    NOT NULL DEFAULT NOW()
);
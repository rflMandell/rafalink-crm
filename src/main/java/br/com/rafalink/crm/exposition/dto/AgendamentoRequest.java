package br.com.rafalink.crm.exposition.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendamentoRequest(

        @NotBlank(message = "Título é obrigatório.")
        String titulo,

        String descricao,

        @NotNull(message = "Data e hora são obrigatórias.")
        @Future(message = "A data e hora do agendamento devem ser no futuro.")
        LocalDateTime dataHora,

        @NotNull(message = "ID do usuário responsável é obrigatório.")
        Long usuarioId,

        Long leadId
) {}
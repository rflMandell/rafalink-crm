package br.com.rafalink.crm.exposition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendamentoRequest(

        @NotBlank(message = "Titulo e obrigatorio")
        String titulo,

        String descricao,

        @NotNull(message = "Data e hora sao obrigatorias")
        LocalDateTime dataHora,

        Long leadId,

        @NotNull(message = "Usuario responsavel e obrigatorio")
        Long usuarioId
) {}

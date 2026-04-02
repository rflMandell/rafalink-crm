package br.com.rafalink.crm.exposition.dto;

import br.com.rafalink.crm.domain.model.CanalCampanha;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CampanhaRequest(

        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        String descricao,

        @NotNull(message = "Canal é obrigatório.")
        CanalCampanha canal,

        @NotNull(message = "Data de início é obrigatória.")
        LocalDate inicio,

        LocalDate fim
) {}
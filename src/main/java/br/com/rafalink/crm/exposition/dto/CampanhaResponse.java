package br.com.rafalink.crm.exposition.dto;

import br.com.rafalink.crm.domain.model.CanalCampanha;
import br.com.rafalink.crm.domain.model.Campanha;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CampanhaResponse(
        Long id,
        String nome,
        String descricao,
        CanalCampanha canal,
        LocalDate inicio,
        LocalDate fim,
        Boolean ativa,
        LocalDateTime criadoEm
) {
    public static CampanhaResponse from(Campanha c) {
        return new CampanhaResponse(
                c.getId(),
                c.getNome(),
                c.getDescricao(),
                c.getCanal(),
                c.getInicio(),
                c.getFim(),
                c.getAtiva(),
                c.getCriadoEm()
        );
    }
}
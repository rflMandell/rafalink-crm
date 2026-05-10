package br.com.rafalink.crm.exposition.dto;
import br.com.rafalink.crm.domain.model.*;
import java.time.LocalDateTime;
public record AgendamentoResponse(Long id, String titulo, String descricao, LocalDateTime dataHora,
                                  StatusAgendamento status, Long leadId, String leadNome,
                                  Long usuarioId, String usuarioNome, LocalDateTime criadoEm) {
    public static AgendamentoResponse from(Agendamento a) {
        return new AgendamentoResponse(a.getId(), a.getTitulo(), a.getDescricao(), a.getDataHora(),
                a.getStatus(),
                a.getLead() != null ? a.getLead().getId() : null,
                a.getLead() != null ? a.getLead().getNome() : null,
                a.getUsuario() != null ? a.getUsuario().getId() : null,
                a.getUsuario() != null ? a.getUsuario().getNome() : null,
                a.getCriadoEm());
    }
}

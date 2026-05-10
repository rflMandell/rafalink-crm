package br.com.rafalink.crm.exposition.dto;
import br.com.rafalink.crm.domain.model.*;
import java.time.LocalDateTime;
public record LeadResponse(Long id, String nome, String email, String telefone, String origem,
                           StatusLead status, Boolean arquivado, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
    public static LeadResponse from(Lead l) {
        return new LeadResponse(l.getId(), l.getNome(), l.getEmail(), l.getTelefone(), l.getOrigem(),
                l.getStatus(), l.getArquivado(), l.getCriadoEm(), l.getAtualizadoEm());
    }
}

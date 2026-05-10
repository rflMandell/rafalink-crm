package br.com.rafalink.crm.application.service;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import br.com.rafalink.crm.domain.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j @Service @RequiredArgsConstructor
public class LeadService {
    private final LeadRepository leadRepository;

    @Transactional
    public Lead cadastrar(Lead l) {
        if (leadRepository.existsByEmail(l.getEmail()))
            throw new BusinessException("Já existe um lead com o e-mail: " + l.getEmail());
        l.setStatus(StatusLead.NOVO);
        l.setCriadoEm(LocalDateTime.now());
        l.setAtualizadoEm(LocalDateTime.now());
        l.setArquivado(false);
        return leadRepository.save(l);
    }

    @Transactional
    public Lead atualizarStatus(Long id, StatusLead novoStatus) {
        Lead l = buscarPorId(id);
        if (l.getArquivado()) throw new BusinessException("Não é possível alterar status de lead arquivado.");
        l.setStatus(novoStatus);
        l.setAtualizadoEm(LocalDateTime.now());
        return leadRepository.save(l);
    }

    @Transactional
    public int arquivarLeadsInativos() {
        return leadRepository.arquivarLeadsInativos(LocalDateTime.now().minusDays(90));
    }

    @Transactional(readOnly=true)
    public Lead buscarPorId(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", id));
    }

    @Transactional(readOnly=true)
    public List<Lead> listarTodos() { return leadRepository.findAll(); }

    @Transactional(readOnly=true)
    public List<Lead> listarPorStatus(StatusLead status) { return leadRepository.findByStatus(status); }
}

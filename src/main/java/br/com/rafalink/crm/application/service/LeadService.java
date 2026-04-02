package br.com.rafalink.crm.application.service;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.InvalidStatusException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import br.com.rafalink.crm.domain.model.Lead;
import br.com.rafalink.crm.domain.model.StatusLead;
import br.com.rafalink.crm.domain.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;

    // Lead deve ter e-mail válido e único.
    // Status inicial sempre NOVO
    @Transactional
    public Lead cadastrar(Lead lead) {
        log.debug("Cadastrando lead: {}", lead.getEmail());

        if (lead.getEmail() == null || !lead.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BusinessException("Lead deve ter um e-mail válido.");
        }

        if (leadRepository.existsByEmail(lead.getEmail())) {
            throw new BusinessException(
                    "Já existe um lead com o e-mail: " + lead.getEmail()
            );
        }

        // Garante que o status inicial é sempre NOVO
        lead.setStatus(StatusLead.NOVO);
        lead.setCriadoEm(LocalDateTime.now());
        lead.setAtualizadoEm(LocalDateTime.now());
        lead.setArquivado(false);

        Lead salvo = leadRepository.save(lead);
        log.debug("Lead cadastrado. ID: {}", salvo.getId());
        return salvo;
    }

    // Transição de status só é permitida se o lead não estiver arquivado.
    // Status deve ser um valor válido do enum
    @Transactional
    public Lead atualizarStatus(Long id, StatusLead novoStatus) {
        Lead lead = buscarPorId(id);

        if (lead.getArquivado()) {
            throw new BusinessException(
                    "Não é possível alterar o status de um lead arquivado."
            );
        }

        if (novoStatus == null) {
            throw new InvalidStatusException("nulo");
        }

        log.debug("Lead ID {} mudando status: {} → {}", id, lead.getStatus(), novoStatus);

        lead.setStatus(novoStatus);
        lead.setAtualizadoEm(LocalDateTime.now());
        return leadRepository.save(lead);
    }

    // Leads sem atualização há mais de 90 dias são arquivados.
    @Transactional
    public int arquivarLeadsInativos() {
        LocalDateTime limite = LocalDateTime.now().minusDays(90);
        int total = leadRepository.arquivarLeadsInativos(limite);
        log.debug("{} leads arquivados por inatividade (>90 dias).", total);
        return total;
    }

    @Transactional(readOnly = true)
    public Lead buscarPorId(Long id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead", id));
    }

    @Transactional(readOnly = true)
    public List<Lead> listarTodos() {
        return leadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Lead> listarPorStatus(StatusLead status) {
        return leadRepository.findByStatus(status);
    }
}
package br.com.rafalink.crm.application.service;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import br.com.rafalink.crm.domain.model.Agendamento;
import br.com.rafalink.crm.domain.model.StatusAgendamento;
import br.com.rafalink.crm.domain.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    // Agendamento não pode ser criado em data/hora passada.
    @Transactional
    public Agendamento criar(Agendamento agendamento) {
        log.debug("Criando agendamento: {}", agendamento.getTitulo());

        if (agendamento.getDataHora() == null) {
            throw new BusinessException("A data e hora do agendamento são obrigatórias.");
        }

        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException(
                    "Não é possível criar agendamento em uma data/hora passada."
            );
        }

        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setCriadoEm(LocalDateTime.now());

        Agendamento salvo = agendamentoRepository.save(agendamento);
        log.debug("Agendamento criado. ID: {}", salvo.getId());
        return salvo;
    }

    // Só é possível cancelar agendamentos com status AGENDADO.
    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento agendamento = buscarPorId(id);

        if (!agendamento.getStatus().equals(StatusAgendamento.AGENDADO)) {
            throw new BusinessException(
                    "Só é possível cancelar agendamentos com status AGENDADO. " +
                            "Status atual: " + agendamento.getStatus()
            );
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        log.debug("Agendamento ID {} cancelado.", id);
        return agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", id));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorUsuario(Long usuarioId) {
        return agendamentoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorLead(Long leadId) {
        return agendamentoRepository.findByLeadId(leadId);
    }
}
package br.com.rafalink.crm.application.service;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.*;
import br.com.rafalink.crm.domain.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;

    @Transactional
    public Agendamento criar(Agendamento a) {
        if (a.getDataHora().isBefore(LocalDateTime.now()))
            throw new BusinessException("Não é possível criar agendamento em data/hora passada.");
        a.setStatus(StatusAgendamento.AGENDADO);
        a.setCriadoEm(LocalDateTime.now());
        return agendamentoRepository.save(a);
    }

    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento a = buscarPorId(id);
        if (!StatusAgendamento.AGENDADO.equals(a.getStatus()))
            throw new BusinessException("Só é possível cancelar agendamentos com status AGENDADO. Atual: " + a.getStatus());
        a.setStatus(StatusAgendamento.CANCELADO);
        return agendamentoRepository.save(a);
    }

    @Transactional(readOnly=true)
    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", id));
    }

    @Transactional(readOnly=true)
    public List<Agendamento> listarPorUsuario(Long uid) { return agendamentoRepository.findByUsuarioId(uid); }

    @Transactional(readOnly=true)
    public List<Agendamento> listarPorLead(Long lid) { return agendamentoRepository.findByLeadId(lid); }
}

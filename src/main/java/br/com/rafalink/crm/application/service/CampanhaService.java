package br.com.rafalink.crm.application.service;
import br.com.rafalink.crm.domain.exception.*;
import br.com.rafalink.crm.domain.model.Campanha;
import br.com.rafalink.crm.domain.repository.CampanhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class CampanhaService {
    private final CampanhaRepository campanhaRepository;

    @Transactional
    public Campanha criar(Campanha c) {
        if (campanhaRepository.existsByNome(c.getNome()))
            throw new BusinessException("Já existe uma campanha com o nome: " + c.getNome());
        if (c.getFim() != null && c.getFim().isBefore(c.getInicio()))
            throw new BusinessException("Data de fim não pode ser anterior à data de início.");
        c.setAtiva(true);
        return campanhaRepository.save(c);
    }

    @Transactional
    public Campanha encerrar(Long id) {
        Campanha c = buscarPorId(id);
        if (!c.getAtiva()) throw new BusinessException("Campanha já está encerrada.");
        c.setAtiva(false);
        c.setFim(LocalDate.now());
        return campanhaRepository.save(c);
    }

    @Transactional(readOnly=true)
    public Campanha buscarPorId(Long id) {
        return campanhaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campanha", id));
    }

    @Transactional(readOnly=true)
    public List<Campanha> listarAtivas() { return campanhaRepository.findByAtivaTrue(); }

    @Transactional(readOnly=true)
    public List<Campanha> listarTodas() { return campanhaRepository.findAll(); }
}

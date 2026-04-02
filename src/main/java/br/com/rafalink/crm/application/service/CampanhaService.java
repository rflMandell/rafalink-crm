package br.com.rafalink.crm.application.service;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import br.com.rafalink.crm.domain.model.Campanha;
import br.com.rafalink.crm.domain.repository.CampanhaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;

    // Nome da campanha deve ser único.
    // Data de fim não pode ser anterior à data de início.
    @Transactional
    public Campanha criar(Campanha campanha) {
        log.debug("Criando campanha: {}", campanha.getNome());

        if (campanhaRepository.existsByNome(campanha.getNome())) {
            throw new BusinessException(
                    "Já existe uma campanha com o nome: " + campanha.getNome()
            );
        }

        if (campanha.getFim() != null && campanha.getFim().isBefore(campanha.getInicio())) {
            throw new BusinessException(
                    "A data de fim não pode ser anterior à data de início da campanha."
            );
        }

        campanha.setAtiva(true);
        Campanha salva = campanhaRepository.save(campanha);
        log.debug("Campanha criada. ID: {}", salva.getId());
        return salva;
    }

    // Encerrar campanha define a data de fim como hoje e desativa.
    @Transactional
    public Campanha encerrar(Long id) {
        Campanha campanha = buscarPorId(id);

        if (!campanha.getAtiva()) {
            throw new BusinessException(
                    "A campanha já está encerrada."
            );
        }

        campanha.setAtiva(false);
        campanha.setFim(LocalDate.now());
        log.debug("Campanha ID {} encerrada.", id);
        return campanhaRepository.save(campanha);
    }

    @Transactional(readOnly = true)
    public Campanha buscarPorId(Long id) {
        return campanhaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campanha", id));
    }

    @Transactional(readOnly = true)
    public List<Campanha> listarAtivas() {
        return campanhaRepository.findByAtivaTrue();
    }

    @Transactional(readOnly = true)
    public List<Campanha> listarTodas() {
        return campanhaRepository.findAll();
    }
}
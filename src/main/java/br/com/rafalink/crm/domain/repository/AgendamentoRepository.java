package br.com.rafalink.crm.domain.repository;

import br.com.rafalink.crm.domain.model.Agendamento;
import br.com.rafalink.crm.domain.model.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendamentoRepository {

    List<Campanha> findByAtivaTrue();
    boolean existsByNome(String nome);
}

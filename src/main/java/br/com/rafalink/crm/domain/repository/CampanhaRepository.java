package br.com.rafalink.crm.domain.repository;

import br.com.rafalink.crm.domain.model.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Long> {
    List<Campanha> findByAtivaTrue();
    boolean existsByNome(String nome);
}
package br.com.rafalink.crm.domain.repository;
import br.com.rafalink.crm.domain.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioId(Long usuarioId);
    List<Agendamento> findByLeadId(Long leadId);
}

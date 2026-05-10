package br.com.rafalink.crm.domain.repository;
import br.com.rafalink.crm.domain.model.Lead;
import br.com.rafalink.crm.domain.model.StatusLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    boolean existsByEmail(String email);
    List<Lead> findByStatus(StatusLead status);
    @Modifying
    @Query("UPDATE Lead l SET l.arquivado = true WHERE l.atualizadoEm < :limite AND l.arquivado = false")
    int arquivarLeadsInativos(LocalDateTime limite);
}

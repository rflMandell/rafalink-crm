package br.com.rafalink.crm.domain.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="leads") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Lead {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String nome;
    @Column(nullable=false, unique=true, length=150) private String email;
    @Column(length=20) private String telefone;
    @Column(length=50) private String origem;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private StatusLead status = StatusLead.NOVO;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="responsavel_id") private Usuario responsavel;
    @Column(name="criado_em", nullable=false, updatable=false) private LocalDateTime criadoEm = LocalDateTime.now();
    @Column(name="atualizado_em", nullable=false) private LocalDateTime atualizadoEm = LocalDateTime.now();
    @Column(nullable=false) private Boolean arquivado = false;
}

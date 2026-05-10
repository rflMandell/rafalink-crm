package br.com.rafalink.crm.domain.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name="usuarios") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=100) private String nome;
    @Column(nullable=false, unique=true, length=150) private String email;
    @Column(nullable=false, length=255) private String senha;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private PerfilUsuario perfil;
    @Column(nullable=false) private Boolean ativo = true;
    @Column(name="criado_em", nullable=false, updatable=false) private LocalDateTime criadoEm = LocalDateTime.now();
}

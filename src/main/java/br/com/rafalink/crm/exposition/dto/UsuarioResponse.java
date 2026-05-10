package br.com.rafalink.crm.exposition.dto;
import br.com.rafalink.crm.domain.model.*;
import java.time.LocalDateTime;
public record UsuarioResponse(Long id, String nome, String email, PerfilUsuario perfil, Boolean ativo, LocalDateTime criadoEm) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getPerfil(), u.getAtivo(), u.getCriadoEm());
    }
}

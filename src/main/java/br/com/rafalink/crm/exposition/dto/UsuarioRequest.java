package br.com.rafalink.crm.exposition.dto;
import br.com.rafalink.crm.domain.model.PerfilUsuario;
import jakarta.validation.constraints.*;
public record UsuarioRequest(
    @NotBlank(message="Nome é obrigatório.") String nome,
    @NotBlank @Email(message="E-mail inválido.") String email,
    @NotBlank(message="Senha é obrigatória.") String senha,
    @NotNull(message="Perfil é obrigatório.") PerfilUsuario perfil
) {}

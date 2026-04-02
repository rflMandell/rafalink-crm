package br.com.rafalink.crm.exposition.dto;

import br.com.rafalink.crm.domain.model.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest (

    @NotBlank(message = "Nome e obrigatorio")
    String nome,

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email e obrigatorio")
    String email,

    @NotBlank(message = "Senha e obrigatoria")
    String senha,

    @NotNull(message = "Perfil e obrigatorio")
    PerfilUsuario perfil
) {}

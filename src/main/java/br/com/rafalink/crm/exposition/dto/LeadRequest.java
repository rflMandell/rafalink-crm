package br.com.rafalink.crm.exposition.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LeadRequest(

        @NotBlank(message = "Nome e obrigatorio")
        String nome,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        String telefone,
        String origem
) {}

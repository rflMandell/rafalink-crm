package br.com.rafalink.crm.exposition.dto;
import jakarta.validation.constraints.*;
public record LeadRequest(
    @NotBlank(message="Nome é obrigatório.") String nome,
    @NotBlank @Email(message="E-mail inválido.") String email,
    String telefone, String origem
) {}

package br.com.rafalink.crm.exposition.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record AgendamentoRequest(
    @NotBlank(message="Título é obrigatório.") String titulo,
    String descricao,
    @NotNull @Future(message="A data/hora deve ser no futuro.") LocalDateTime dataHora,
    @NotNull(message="ID do usuário é obrigatório.") Long usuarioId,
    Long leadId
) {}

package br.com.rafalink.crm.exposition.dto;
import java.time.LocalDateTime;
public record ErroResponse(int status, String erro, String mensagem, LocalDateTime timestamp) {}

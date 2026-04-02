package br.com.rafalink.crm.exposition.handler;

import br.com.rafalink.crm.domain.exception.BusinessException;
import br.com.rafalink.crm.domain.exception.InvalidStatusException;
import br.com.rafalink.crm.domain.exception.ResourceNotFoundException;
import br.com.rafalink.crm.exposition.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErroResponse(404, "Não encontrado", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new ErroResponse(422, "Regra de negócio violada", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ErroResponse> handleInvalidStatus(InvalidStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErroResponse(400, "Status inválido", ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErroResponse(400, "Erro de validação", mensagem, LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErroResponse(500, "Erro interno", ex.getMessage(), LocalDateTime.now())
        );
    }
}
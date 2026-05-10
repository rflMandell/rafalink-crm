package br.com.rafalink.crm.domain.exception;
public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String s) {
        super("Status '" + s + "' inválido. Use: NOVO, EM_CONTATO, CONVERTIDO ou PERDIDO");
    }
}

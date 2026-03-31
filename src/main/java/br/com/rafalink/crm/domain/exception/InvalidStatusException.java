package br.com.rafalink.crm.domain.exception;

public class InvalidStatusException extends RuntimeException{

    public InvalidStatusException(String statusInformado) {
        super(String.format(
                "Status '%s' invalido. Use: NOVO, EM_CONTATO, CONVERTIDO ou PERDIDO",
                statusInformado
        ));
    }
}

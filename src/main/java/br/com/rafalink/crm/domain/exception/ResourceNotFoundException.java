package br.com.rafalink.crm.domain.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String recurso, Long id) {
        super(String.format("%s com id %d nao encontrado.", recurso, id));
    }
}

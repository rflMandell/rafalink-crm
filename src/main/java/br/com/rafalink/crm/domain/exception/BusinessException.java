package br.com.rafalink.crm.domain.exception;
public class BusinessException extends RuntimeException {
    public BusinessException(String mensagem) { super(mensagem); }
}

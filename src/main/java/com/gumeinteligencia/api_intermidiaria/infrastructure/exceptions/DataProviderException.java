package com.gumeinteligencia.api_intermidiaria.infrastructure.exceptions;

public class DataProviderException extends RuntimeException {
    public DataProviderException(String mensagem, Throwable cause) {
        super(mensagem, cause);
    }
}

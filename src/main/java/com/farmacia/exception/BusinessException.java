package com.farmacia.exception;

/** Regla de negocio incumplida (HTTP 400). Provoca rollback de la transacción. */
public class BusinessException extends RuntimeException {
    public BusinessException(String mensaje) {
        super(mensaje);
    }
}

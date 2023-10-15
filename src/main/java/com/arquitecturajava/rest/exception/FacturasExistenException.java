package com.arquitecturajava.rest.exception;

public class FacturasExistenException extends RuntimeException {

    public FacturasExistenException(String message) {
        super(message);
    }
}
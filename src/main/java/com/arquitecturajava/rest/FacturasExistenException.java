package com.arquitecturajava.rest;

public class FacturasExistenException extends RuntimeException {

    public FacturasExistenException(String message) {
        super(message);
    }
}
package com.djole.prodavnica.service.exception;

public class NeautentifikovanException extends RuntimeException {
    public NeautentifikovanException(String poruka) {
        super(poruka);
    }
}

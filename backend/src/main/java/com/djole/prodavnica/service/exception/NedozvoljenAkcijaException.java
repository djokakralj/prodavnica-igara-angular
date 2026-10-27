package com.djole.prodavnica.service.exception;

public class NedozvoljenAkcijaException extends RuntimeException {
    public NedozvoljenAkcijaException(String poruka) {
        super(poruka);
    }
}

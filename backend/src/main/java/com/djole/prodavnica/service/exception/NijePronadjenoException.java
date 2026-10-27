package com.djole.prodavnica.service.exception;

public class NijePronadjenoException extends RuntimeException {
    public NijePronadjenoException(String poruka) {
        super(poruka);
    }
}

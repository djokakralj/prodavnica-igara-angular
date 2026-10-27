package com.djole.prodavnica.service.exception;

public class NevalidniPodaciException extends RuntimeException {
    public NevalidniPodaciException(String poruka) {
        super(poruka);
    }
}

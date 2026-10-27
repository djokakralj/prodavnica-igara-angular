package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.service.exception.NeautentifikovanException;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NeautentifikovanException.class)
    public ResponseEntity<Response> handleNeautentifikovan(NeautentifikovanException e) {
        return odgovor(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NedozvoljenAkcijaException.class)
    public ResponseEntity<Response> handleNedozvoljenaAkcija(NedozvoljenAkcijaException e) {
        return odgovor(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NijePronadjenoException.class)
    public ResponseEntity<Response> handleNijePronadjeno(NijePronadjenoException e) {
        return odgovor(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NevalidniPodaciException.class)
    public ResponseEntity<Response> handleNevalidniPodaci(NevalidniPodaciException e) {
        return odgovor(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleOstalo(Exception e) {
        log.error("Neočekivana greška pri obradi zahteva", e);
        return odgovor("Došlo je do neočekivane greške.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Response> odgovor(String poruka, HttpStatus status) {
        return ResponseEntity.status(status).body(HttpResponse.getResponse(poruka, status));
    }
}

package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.KorisnikDTO;
import com.djole.prodavnica.dto.PrijavaDTO;
import com.djole.prodavnica.dto.RegistracijaDTO;
import com.djole.prodavnica.mapper.KorisnikMapper;
import com.djole.prodavnica.model.Korisnik;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.service.AutentifikacijaService;
import com.djole.prodavnica.service.exception.NeautentifikovanException;
import com.djole.prodavnica.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutentifikacijaService autentifikacijaService;
    private final KorisnikMapper korisnikMapper;

    public AuthController(AutentifikacijaService autentifikacijaService, KorisnikMapper korisnikMapper) {
        this.autentifikacijaService = autentifikacijaService;
        this.korisnikMapper = korisnikMapper;
    }

    @PostMapping("/registracija")
    public ResponseEntity<Response> registracija(@RequestBody RegistracijaDTO zahtev, HttpServletRequest req) {
        Kupac kupac = autentifikacijaService.registrujKupca(zahtev.getKorisnickoIme(), zahtev.getLozinka(),
                zahtev.getEmail(), zahtev.getAdresa(), zahtev.getBrojTelefona());
        SessionUtil.prijaviKorisnika(req, kupac);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Nalog je uspešno kreiran!",
                        Map.of("value", korisnikMapper.toDomainDTO(kupac)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/prijava")
    public ResponseEntity<Response> prijava(@RequestBody PrijavaDTO zahtev, HttpServletRequest req) {
        Korisnik korisnik = autentifikacijaService.prijaviSe(zahtev.getKorisnickoIme(), zahtev.getLozinka());
        SessionUtil.prijaviKorisnika(req, korisnik);

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Dobro došli, " + korisnik.getKorisnickoIme() + "!",
                        Map.of("value", korisnikMapper.toDomainDTO(korisnik)),
                        HttpStatus.OK));
    }

    @PostMapping("/odjava")
    public ResponseEntity<Response> odjava(HttpServletRequest req) {
        SessionUtil.odjaviKorisnika(req);
        return ResponseEntity.ok(HttpResponse.getResponse("Uspešno ste se odjavili!", HttpStatus.OK));
    }

    @GetMapping("/ja")
    public ResponseEntity<Response> ja(HttpServletRequest req) {
        if (!SessionUtil.jePrijavljen(req)) {
            throw new NeautentifikovanException("Niste prijavljeni.");
        }
        KorisnikDTO dto = new KorisnikDTO();
        dto.setId(SessionUtil.trenutniIdKorisnika(req));
        dto.setKorisnickoIme(SessionUtil.trenutnoKorisnickoIme(req));
        dto.setUloga(SessionUtil.trenutnaUloga(req));

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Korisnik je pronađen!",
                        Map.of("value", dto),
                        HttpStatus.OK));
    }
}

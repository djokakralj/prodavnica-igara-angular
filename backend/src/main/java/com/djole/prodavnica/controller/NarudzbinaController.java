package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.NarudzbinaDTO;
import com.djole.prodavnica.dto.StatusDTO;
import com.djole.prodavnica.mapper.NarudzbinaMapper;
import com.djole.prodavnica.model.Narudzbina;
import com.djole.prodavnica.model.StatusNarudzbine;
import com.djole.prodavnica.service.NarudzbinaPdfService;
import com.djole.prodavnica.service.NarudzbinaService;
import com.djole.prodavnica.service.StavkaZahtev;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.util.KorpaUtil;
import com.djole.prodavnica.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/narudzbine")
public class NarudzbinaController {

    private final NarudzbinaService narudzbinaService;
    private final NarudzbinaMapper narudzbinaMapper;
    private final NarudzbinaPdfService narudzbinaPdfService;

    public NarudzbinaController(NarudzbinaService narudzbinaService, NarudzbinaMapper narudzbinaMapper,
                                NarudzbinaPdfService narudzbinaPdfService) {
        this.narudzbinaService = narudzbinaService;
        this.narudzbinaMapper = narudzbinaMapper;
        this.narudzbinaPdfService = narudzbinaPdfService;
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addNarudzbina(HttpServletRequest req) {
        Long idKupac = SessionUtil.zahtevajKupca(req);
        List<StavkaZahtev> stavke = KorpaUtil.korpa(req).entrySet().stream()
                .map(unos -> new StavkaZahtev(unos.getKey(), unos.getValue()))
                .collect(Collectors.toList());

        Narudzbina narudzbina = narudzbinaService.napraviNarudzbinu(idKupac, stavke);
        KorpaUtil.isprazni(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Narudžbina je uspešno kreirana!",
                        Map.of("value", narudzbinaMapper.toDomainDTO(narudzbina)),
                        HttpStatus.CREATED));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(HttpServletRequest req) {
        SessionUtil.zahtevajPrijavu(req);
        List<Narudzbina> narudzbine = SessionUtil.jeAdmin(req)
                ? narudzbinaService.sveNarudzbine()
                : narudzbinaService.narudzbineKupca(SessionUtil.trenutniIdKorisnika(req));

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Narudžbine su pronađene!",
                        Map.of("values", uDtoListu(narudzbine)),
                        HttpStatus.OK));
    }

    @GetMapping("/getNarudzbina/{narudzbinaId}")
    public ResponseEntity<Response> findNarudzbinaById(@PathVariable("narudzbinaId") Long narudzbinaId,
                                                       HttpServletRequest req) {
        SessionUtil.zahtevajPrijavu(req);
        Narudzbina narudzbina = narudzbinaService.nadjiPoId(narudzbinaId);

        boolean vlasnik = narudzbina.getKupac().getIdKorisnik().equals(SessionUtil.trenutniIdKorisnika(req));
        if (!vlasnik && !SessionUtil.jeAdmin(req)) {
            throw new NedozvoljenAkcijaException("Nemate pristup ovoj narudžbini.");
        }

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Narudžbina je pronađena!",
                        Map.of("value", narudzbinaMapper.toDomainDTO(narudzbina)),
                        HttpStatus.OK));
    }

    @GetMapping(value = "/pdf/{narudzbinaId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> pdfNarudzbine(@PathVariable("narudzbinaId") Long narudzbinaId,
                                                HttpServletRequest req) {
        SessionUtil.zahtevajPrijavu(req);
        Narudzbina narudzbina = narudzbinaService.nadjiPoId(narudzbinaId);

        boolean vlasnik = narudzbina.getKupac().getIdKorisnik().equals(SessionUtil.trenutniIdKorisnika(req));
        if (!vlasnik && !SessionUtil.jeAdmin(req)) {
            throw new NedozvoljenAkcijaException("Nemate pristup ovoj narudžbini.");
        }

        byte[] pdf = narudzbinaPdfService.generisi(narudzbina);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"narudzbina-" + narudzbinaId + ".pdf\"")
                .body(pdf);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<Response> updateStatus(@RequestBody StatusDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        StatusNarudzbine status;
        try {
            status = StatusNarudzbine.valueOf(zahtev.getStatus());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new NevalidniPodaciException("Nepoznat status narudžbine.");
        }

        Narudzbina narudzbina = narudzbinaService.promeniStatus(zahtev.getId(), status);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Status narudžbine je uspešno promenjen!",
                        Map.of("value", narudzbinaMapper.toDomainDTO(narudzbina)),
                        HttpStatus.OK));
    }

    private List<NarudzbinaDTO> uDtoListu(List<Narudzbina> narudzbine) {
        return narudzbine.stream().map(narudzbinaMapper::toDomainDTO).collect(Collectors.toList());
    }
}

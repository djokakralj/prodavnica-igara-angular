package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.KorpaDTO;
import com.djole.prodavnica.dto.KorpaStavkaDTO;
import com.djole.prodavnica.dto.StavkaKorpeDTO;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.service.IgraService;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.util.KorpaUtil;
import com.djole.prodavnica.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/korpa")
public class KorpaController {

    private final IgraService igraService;

    public KorpaController(IgraService igraService) {
        this.igraService = igraService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getKorpa(HttpServletRequest req) {
        SessionUtil.zahtevajKupca(req);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Korpa je učitana!",
                        Map.of("value", izgradiKorpu(req)),
                        HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addStavka(@RequestBody StavkaKorpeDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajKupca(req);
        if (zahtev.getIdIgra() == null || zahtev.getKolicina() == null || zahtev.getKolicina() <= 0) {
            throw new NevalidniPodaciException("idIgra i pozitivna količina su obavezni.");
        }
        Igra igra = igraService.nadjiPoId(zahtev.getIdIgra());

        Map<Long, Integer> korpa = KorpaUtil.korpa(req);
        int trazeno = korpa.getOrDefault(zahtev.getIdIgra(), 0) + zahtev.getKolicina();
        proveriStanje(igra, trazeno);

        korpa.put(zahtev.getIdIgra(), trazeno);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Igra je uspešno dodata u korpu!",
                        Map.of("value", izgradiKorpu(req)),
                        HttpStatus.OK));
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateStavka(@RequestBody StavkaKorpeDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajKupca(req);
        if (zahtev.getIdIgra() == null || zahtev.getKolicina() == null || zahtev.getKolicina() <= 0) {
            throw new NevalidniPodaciException("Količina mora biti pozitivna.");
        }
        Igra igra = igraService.nadjiPoId(zahtev.getIdIgra());
        proveriStanje(igra, zahtev.getKolicina());

        KorpaUtil.korpa(req).put(zahtev.getIdIgra(), zahtev.getKolicina());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Korpa je uspešno izmenjena!",
                        Map.of("value", izgradiKorpu(req)),
                        HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> deleteStavka(@RequestBody Long idIgra, HttpServletRequest req) {
        SessionUtil.zahtevajKupca(req);
        KorpaUtil.korpa(req).remove(idIgra);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Igra je uspešno uklonjena iz korpe!",
                        Map.of("value", izgradiKorpu(req)),
                        HttpStatus.OK));
    }

    private static void proveriStanje(Igra igra, int trazenaKolicina) {
        if (igra.getKolicinaNaStanju() < trazenaKolicina) {
            throw new NevalidniPodaciException("Nema dovoljno igre '" + igra.getNaziv() + "' na stanju.");
        }
    }

    private KorpaDTO izgradiKorpu(HttpServletRequest req) {
        Map<Long, Integer> korpa = KorpaUtil.korpa(req);
        List<KorpaStavkaDTO> stavke = new ArrayList<>();
        BigDecimal ukupno = BigDecimal.ZERO;

        Map<Long, Igra> igre = igraService.nadjiPoIdevima(korpa.keySet());

        Iterator<Map.Entry<Long, Integer>> prolaz = korpa.entrySet().iterator();
        while (prolaz.hasNext()) {
            Map.Entry<Long, Integer> unos = prolaz.next();
            Igra igra = igre.get(unos.getKey());
            if (igra == null) {
                prolaz.remove();
                continue;
            }
            BigDecimal podukupno = igra.getCena().multiply(BigDecimal.valueOf(unos.getValue()));
            stavke.add(new KorpaStavkaDTO(igra.getIdIgra(), igra.getNaziv(), igra.getCena(),
                    unos.getValue(), podukupno, igra.getKolicinaNaStanju()));
            ukupno = ukupno.add(podukupno);
        }
        return new KorpaDTO(stavke, ukupno);
    }
}

package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.RecenzijaDTO;
import com.djole.prodavnica.mapper.RecenzijaMapper;
import com.djole.prodavnica.model.Recenzija;
import com.djole.prodavnica.service.RecenzijaService;
import com.djole.prodavnica.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/recenzije")
public class RecenzijaController {

    private final RecenzijaService recenzijaService;
    private final RecenzijaMapper recenzijaMapper;

    public RecenzijaController(RecenzijaService recenzijaService, RecenzijaMapper recenzijaMapper) {
        this.recenzijaService = recenzijaService;
        this.recenzijaMapper = recenzijaMapper;
    }

    @GetMapping("/zaIgru/{igraId}")
    public ResponseEntity<Response> getZaIgru(@PathVariable("igraId") Long igraId) {
        List<RecenzijaDTO> dtos = recenzijaService.recenzijeZaIgru(igraId).stream()
                .map(recenzijaMapper::toDomainDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Recenzije su pronađene!",
                        Map.of("values", dtos),
                        HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addRecenzija(@RequestBody RecenzijaDTO recenzijaDTO, HttpServletRequest req) {
        Long idKupac = SessionUtil.zahtevajKupca(req);
        Recenzija recenzija = recenzijaService.ostaviRecenziju(idKupac, recenzijaDTO.getIdIgra(),
                recenzijaDTO.getOcena(), recenzijaDTO.getKomentar());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Recenzija je uspešno sačuvana!",
                        Map.of("value", recenzijaMapper.toDomainDTO(recenzija)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateRecenzija(@RequestBody RecenzijaDTO recenzijaDTO, HttpServletRequest req) {
        Long idKupac = SessionUtil.zahtevajKupca(req);
        Recenzija recenzija = recenzijaService.izmeniRecenziju(recenzijaDTO.getId(), idKupac,
                recenzijaDTO.getOcena(), recenzijaDTO.getKomentar());

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Recenzija je uspešno izmenjena!",
                        Map.of("value", recenzijaMapper.toDomainDTO(recenzija)),
                        HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> deleteRecenzija(@RequestBody Long recenzijaId, HttpServletRequest req) {
        Long idKupac = SessionUtil.zahtevajKupca(req);
        recenzijaService.obrisiRecenziju(recenzijaId, idKupac);
        return ResponseEntity.ok(
                HttpResponse.getResponse("Recenzija je uspešno obrisana!", HttpStatus.OK));
    }
}

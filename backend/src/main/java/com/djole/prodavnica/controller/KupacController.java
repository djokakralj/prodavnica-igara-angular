package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.BlokirajDTO;
import com.djole.prodavnica.dto.KorisnikDTO;
import com.djole.prodavnica.mapper.KorisnikMapper;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.service.KorisnikAdminService;
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
@RequestMapping("/kupci")
public class KupacController {

    private final KorisnikAdminService korisnikAdminService;
    private final KorisnikMapper korisnikMapper;

    public KupacController(KorisnikAdminService korisnikAdminService, KorisnikMapper korisnikMapper) {
        this.korisnikAdminService = korisnikAdminService;
        this.korisnikMapper = korisnikMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Kupci su pronađeni!",
                        Map.of("values", uDtoListu(korisnikAdminService.pretraziKupce(null))),
                        HttpStatus.OK));
    }

    @GetMapping("/search/{searchRequest}")
    public ResponseEntity<Response> searchKupci(@PathVariable("searchRequest") String searchRequest,
                                                HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezultati pretrage kupaca:",
                        Map.of("values", uDtoListu(korisnikAdminService.pretraziKupce(searchRequest))),
                        HttpStatus.OK));
    }

    @PostMapping("/blokiraj")
    public ResponseEntity<Response> blokiraj(@RequestBody BlokirajDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Kupac kupac = korisnikAdminService.postaviBlokiran(zahtev.getId(), zahtev.isBlokiran());

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        zahtev.isBlokiran() ? "Kupac je uspešno blokiran!" : "Blokada kupca je uspešno uklonjena!",
                        Map.of("value", korisnikMapper.toDomainDTO(kupac)),
                        HttpStatus.OK));
    }

    private List<KorisnikDTO> uDtoListu(List<Kupac> kupci) {
        return kupci.stream()
                .map(korisnikMapper::toDomainDTO)
                .collect(Collectors.toList());
    }
}

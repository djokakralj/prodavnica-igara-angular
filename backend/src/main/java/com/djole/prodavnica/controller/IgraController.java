package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.IgraDTO;
import com.djole.prodavnica.dto.IgraPlatformaZahtevDTO;
import com.djole.prodavnica.mapper.IgraMapper;
import com.djole.prodavnica.mapper.IgraPlatformaMapper;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.model.IgraPlatforma;
import com.djole.prodavnica.service.IgraService;
import com.djole.prodavnica.service.RecenzijaService;
import com.djole.prodavnica.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/igre")
public class IgraController {

    private final IgraService igraService;
    private final RecenzijaService recenzijaService;
    private final IgraMapper igraMapper;
    private final IgraPlatformaMapper igraPlatformaMapper;

    public IgraController(IgraService igraService, RecenzijaService recenzijaService,
                          IgraMapper igraMapper, IgraPlatformaMapper igraPlatformaMapper) {
        this.igraService = igraService;
        this.recenzijaService = recenzijaService;
        this.igraMapper = igraMapper;
        this.igraPlatformaMapper = igraPlatformaMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Igre su pronađene!",
                        Map.of("values", uDtoListu(igraService.sveIgre())),
                        HttpStatus.OK));
    }

    @GetMapping("/search/{searchRequest}")
    public ResponseEntity<Response> searchIgre(@PathVariable("searchRequest") String searchRequest) {
        List<Igra> igre = igraService.pretraziIgre(searchRequest, null, null, null, null);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezultati pretrage igara:",
                        Map.of("values", uDtoListu(igre)),
                        HttpStatus.OK));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> filterIgre(@RequestParam(name = "naziv", required = false) String naziv,
                                               @RequestParam(name = "zanr", required = false) String zanr,
                                               @RequestParam(name = "idPlatforma", required = false) Long idPlatforma,
                                               @RequestParam(name = "minCena", required = false) BigDecimal minCena,
                                               @RequestParam(name = "maxCena", required = false) BigDecimal maxCena,
                                               @RequestParam(name = "sort", required = false) String sort,
                                               @RequestParam(name = "smer", required = false) String smer,
                                               @RequestParam(name = "strana", required = false) Integer strana,
                                               @RequestParam(name = "velicina", required = false) Integer velicina) {
        Page<Igra> stranica = igraService.pretraziIgre(naziv, zanr, idPlatforma, minCena, maxCena,
                sort, smer, strana, velicina);

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Filtrirane igre:",
                        Map.of("values", uDtoListu(stranica.getContent()),
                                "strana", stranica.getNumber(),
                                "velicina", stranica.getSize(),
                                "ukupnoElemenata", stranica.getTotalElements(),
                                "ukupnoStrana", stranica.getTotalPages()),
                        HttpStatus.OK));
    }

    @GetMapping("/getIgra/{igraId}")
    public ResponseEntity<Response> findIgraById(@PathVariable("igraId") Long igraId) {
        Igra igra = igraService.nadjiPoId(igraId);
        IgraDTO dto = igraMapper.toDomainDTO(igra, recenzijaService.prosecnaOcena(igraId));
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Igra je pronađena!",
                        Map.of("value", dto),
                        HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addIgra(@RequestBody IgraDTO igraDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Igra igra = igraService.kreiraj(igraDTO.getNaziv(), igraDTO.getOpis(), igraDTO.getCena(),
                igraDTO.getGodinaIzdanja(), igraDTO.getZanr(), igraDTO.getKolicinaNaStanju(),
                igraMapper.idIzdavaca(igraDTO));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Igra je uspešno dodata!",
                        Map.of("value", igraMapper.toDomainDTO(igra)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateIgra(@RequestBody IgraDTO igraDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Igra igra = igraService.izmeni(igraDTO.getId(), igraDTO.getNaziv(), igraDTO.getOpis(), igraDTO.getCena(),
                igraDTO.getGodinaIzdanja(), igraDTO.getZanr(), igraDTO.getKolicinaNaStanju(),
                igraMapper.idIzdavaca(igraDTO));

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Igra je uspešno izmenjena!",
                        Map.of("value", igraMapper.toDomainDTO(igra)),
                        HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> deleteIgra(@RequestBody Long igraId, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        igraService.obrisi(igraId);
        return ResponseEntity.ok(
                HttpResponse.getResponse("Igra je uspešno obrisana!", HttpStatus.OK));
    }

    @PostMapping("/addPlatforma")
    public ResponseEntity<Response> addPlatforma(@RequestBody IgraPlatformaZahtevDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        IgraPlatforma veza = igraService.dodajNaPlatformu(zahtev.getIdIgra(), zahtev.getIdPlatforma(),
                zahtev.getDatumPortovanja(), zahtev.isEkskluzivno());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Igra je uspešno dodata na platformu!",
                        Map.of("value", igraPlatformaMapper.toDomainDTO(veza)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/deletePlatforma")
    public ResponseEntity<Response> deletePlatforma(@RequestBody IgraPlatformaZahtevDTO zahtev, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        igraService.ukloniSaPlatforme(zahtev.getIdIgra(), zahtev.getIdPlatforma());
        return ResponseEntity.ok(
                HttpResponse.getResponse("Igra je uspešno uklonjena sa platforme!", HttpStatus.OK));
    }

    private List<IgraDTO> uDtoListu(List<Igra> igre) {
        return igre.stream()
                .map(igra -> igraMapper.toDomainDTO(igra, recenzijaService.prosecnaOcena(igra.getIdIgra())))
                .collect(Collectors.toList());
    }
}

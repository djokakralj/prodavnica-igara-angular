package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.IzdavacDTO;
import com.djole.prodavnica.mapper.IzdavacMapper;
import com.djole.prodavnica.model.Izdavac;
import com.djole.prodavnica.service.IzdavacService;
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
@RequestMapping("/izdavaci")
public class IzdavacController {

    private final IzdavacService izdavacService;
    private final IzdavacMapper izdavacMapper;

    public IzdavacController(IzdavacService izdavacService, IzdavacMapper izdavacMapper) {
        this.izdavacService = izdavacService;
        this.izdavacMapper = izdavacMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Izdavači su pronađeni!",
                        Map.of("values", uDtoListu(izdavacService.pretrazi(null))),
                        HttpStatus.OK));
    }

    @GetMapping("/search/{searchRequest}")
    public ResponseEntity<Response> searchIzdavaci(@PathVariable("searchRequest") String searchRequest) {
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezultati pretrage izdavača:",
                        Map.of("values", uDtoListu(izdavacService.pretrazi(searchRequest))),
                        HttpStatus.OK));
    }

    @GetMapping("/getIzdavac/{izdavacId}")
    public ResponseEntity<Response> findIzdavacById(@PathVariable("izdavacId") Long izdavacId) {
        Izdavac izdavac = izdavacService.nadjiPoId(izdavacId);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Izdavač je pronađen!",
                        Map.of("value", izdavacMapper.toDomainDTO(izdavac)),
                        HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addIzdavac(@RequestBody IzdavacDTO izdavacDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Izdavac izdavac = izdavacService.kreiraj(izdavacDTO.getNaziv(), izdavacDTO.getDrzava());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Izdavač je uspešno dodat!",
                        Map.of("value", izdavacMapper.toDomainDTO(izdavac)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateIzdavac(@RequestBody IzdavacDTO izdavacDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Izdavac izdavac = izdavacService.izmeni(izdavacDTO.getId(), izdavacDTO.getNaziv(), izdavacDTO.getDrzava());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Izdavač je uspešno izmenjen!",
                        Map.of("value", izdavacMapper.toDomainDTO(izdavac)),
                        HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> deleteIzdavac(@RequestBody Long izdavacId, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        izdavacService.obrisi(izdavacId);
        return ResponseEntity.ok(
                HttpResponse.getResponse("Izdavač je uspešno obrisan!", HttpStatus.OK));
    }

    private List<IzdavacDTO> uDtoListu(List<Izdavac> izdavaci) {
        return izdavaci.stream().map(izdavacMapper::toDomainDTO).collect(Collectors.toList());
    }
}

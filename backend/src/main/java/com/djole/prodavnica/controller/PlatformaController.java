package com.djole.prodavnica.controller;

import com.djole.prodavnica.connection.HttpResponse;
import com.djole.prodavnica.connection.Response;
import com.djole.prodavnica.dto.PlatformaDTO;
import com.djole.prodavnica.mapper.PlatformaMapper;
import com.djole.prodavnica.model.Platforma;
import com.djole.prodavnica.service.PlatformaService;
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
@RequestMapping("/platforme")
public class PlatformaController {

    private final PlatformaService platformaService;
    private final PlatformaMapper platformaMapper;

    public PlatformaController(PlatformaService platformaService, PlatformaMapper platformaMapper) {
        this.platformaService = platformaService;
        this.platformaMapper = platformaMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        List<PlatformaDTO> dtos = platformaService.svePlatforme().stream()
                .map(platformaMapper::toDomainDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Platforme su pronađene!",
                        Map.of("values", dtos),
                        HttpStatus.OK));
    }

    @GetMapping("/getPlatforma/{platformaId}")
    public ResponseEntity<Response> findPlatformaById(@PathVariable("platformaId") Long platformaId) {
        Platforma platforma = platformaService.nadjiPoId(platformaId);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Platforma je pronađena!",
                        Map.of("value", platformaMapper.toDomainDTO(platforma)),
                        HttpStatus.OK));
    }

    @PostMapping("/add")
    public ResponseEntity<Response> addPlatforma(@RequestBody PlatformaDTO platformaDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Platforma platforma = platformaService.kreiraj(platformaDTO.getNaziv());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HttpResponse.getResponseWithData(
                        "Platforma je uspešno dodata!",
                        Map.of("value", platformaMapper.toDomainDTO(platforma)),
                        HttpStatus.CREATED));
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updatePlatforma(@RequestBody PlatformaDTO platformaDTO, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        Platforma platforma = platformaService.izmeni(platformaDTO.getId(), platformaDTO.getNaziv());
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Platforma je uspešno izmenjena!",
                        Map.of("value", platformaMapper.toDomainDTO(platforma)),
                        HttpStatus.OK));
    }

    @PostMapping("/delete")
    public ResponseEntity<Response> deletePlatforma(@RequestBody Long platformaId, HttpServletRequest req) {
        SessionUtil.zahtevajAdmina(req);
        platformaService.obrisi(platformaId);
        return ResponseEntity.ok(
                HttpResponse.getResponse("Platforma je uspešno obrisana!", HttpStatus.OK));
    }
}

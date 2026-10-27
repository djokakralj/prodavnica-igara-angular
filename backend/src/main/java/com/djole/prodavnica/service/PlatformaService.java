package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.PlatformaRepository;
import com.djole.prodavnica.model.Platforma;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatformaService {

    private final PlatformaRepository platformaRepository;

    public PlatformaService(PlatformaRepository platformaRepository) {
        this.platformaRepository = platformaRepository;
    }

    @Transactional(readOnly = true)
    public List<Platforma> svePlatforme() {
        return platformaRepository.findAllByOrderByNaziv();
    }

    @Transactional(readOnly = true)
    public Platforma nadjiPoId(Long id) {
        return platformaRepository.findById(id)
                .orElseThrow(() -> new NijePronadjenoException("Platforma ne postoji."));
    }

    @Transactional
    public Platforma kreiraj(String naziv) {
        validirajNaziv(naziv);
        proveriJedinstvenost(naziv, null);
        return platformaRepository.save(new Platforma(naziv));
    }

    @Transactional
    public Platforma izmeni(Long id, String naziv) {
        Platforma platforma = nadjiPoId(id);
        validirajNaziv(naziv);
        proveriJedinstvenost(naziv, id);
        platforma.setNaziv(naziv);
        return platformaRepository.save(platforma);
    }

    @Transactional
    public void obrisi(Long id) {
        platformaRepository.delete(nadjiPoId(id));
    }

    private void validirajNaziv(String naziv) {
        if (naziv == null || naziv.isBlank()) {
            throw new NevalidniPodaciException("Naziv platforme je obavezan.");
        }
    }

    private void proveriJedinstvenost(String naziv, Long idKojaSeMenja) {
        boolean zauzet = (idKojaSeMenja == null)
                ? platformaRepository.existsByNazivIgnoreCase(naziv)
                : platformaRepository.existsByNazivIgnoreCaseAndIdPlatformaNot(naziv, idKojaSeMenja);

        if (zauzet) {
            throw new NevalidniPodaciException("Platforma sa nazivom '" + naziv + "' već postoji.");
        }
    }
}

package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.IzdavacRepository;
import com.djole.prodavnica.model.Izdavac;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IzdavacService {

    private final IzdavacRepository izdavacRepository;

    public IzdavacService(IzdavacRepository izdavacRepository) {
        this.izdavacRepository = izdavacRepository;
    }

    @Transactional(readOnly = true)
    public List<Izdavac> pretrazi(String naziv) {
        return izdavacRepository.pretrazi((naziv == null || naziv.isBlank()) ? null : naziv);
    }

    @Transactional(readOnly = true)
    public Izdavac nadjiPoId(Long id) {
        return izdavacRepository.findById(id)
                .orElseThrow(() -> new NijePronadjenoException("Izdavač ne postoji."));
    }

    @Transactional
    public Izdavac kreiraj(String naziv, String drzava) {
        validirajNaziv(naziv);
        proveriJedinstvenost(naziv, null);
        return izdavacRepository.save(new Izdavac(naziv, drzava));
    }

    @Transactional
    public Izdavac izmeni(Long id, String naziv, String drzava) {
        Izdavac izdavac = nadjiPoId(id);
        validirajNaziv(naziv);
        proveriJedinstvenost(naziv, id);
        izdavac.setNaziv(naziv);
        izdavac.setDrzava(drzava);
        return izdavacRepository.save(izdavac);
    }

    @Transactional
    public void obrisi(Long id) {
        Izdavac izdavac = nadjiPoId(id);

        int brojIgara = izdavac.getIgre().size();
        if (brojIgara > 0) {
            throw new NevalidniPodaciException("Izdavač '" + izdavac.getNaziv() + "' ne može biti obrisan jer je povezan sa "
                    + brojIgara + (brojIgara == 1 ? " igrom." : " igara.")
                    + " Prvo izmenite ili obrišite te igre.");
        }

        izdavacRepository.delete(izdavac);
    }

    private void validirajNaziv(String naziv) {
        if (naziv == null || naziv.isBlank()) {
            throw new NevalidniPodaciException("Naziv izdavača je obavezan.");
        }
    }

    private void proveriJedinstvenost(String naziv, Long idKojiSeMenja) {
        boolean zauzet = (idKojiSeMenja == null)
                ? izdavacRepository.existsByNazivIgnoreCase(naziv)
                : izdavacRepository.existsByNazivIgnoreCaseAndIdIzdavacNot(naziv, idKojiSeMenja);

        if (zauzet) {
            throw new NevalidniPodaciException("Izdavač sa nazivom '" + naziv + "' već postoji.");
        }
    }
}

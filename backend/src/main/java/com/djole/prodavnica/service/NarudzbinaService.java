package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.IgraRepository;
import com.djole.prodavnica.JPARepo.KupacRepository;
import com.djole.prodavnica.JPARepo.NarudzbinaRepository;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.model.Narudzbina;
import com.djole.prodavnica.model.StatusNarudzbine;
import com.djole.prodavnica.model.StavkaNarudzbine;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NarudzbinaService {

    private final NarudzbinaRepository narudzbinaRepository;
    private final IgraRepository igraRepository;
    private final KupacRepository kupacRepository;

    public NarudzbinaService(NarudzbinaRepository narudzbinaRepository, IgraRepository igraRepository,
                             KupacRepository kupacRepository) {
        this.narudzbinaRepository = narudzbinaRepository;
        this.igraRepository = igraRepository;
        this.kupacRepository = kupacRepository;
    }

    @Transactional
    public Narudzbina napraviNarudzbinu(Long idKupac, List<StavkaZahtev> stavkeZahtev) {
        if (stavkeZahtev == null || stavkeZahtev.isEmpty()) {
            throw new NevalidniPodaciException("Korpa je prazna.");
        }
        Kupac kupac = kupacRepository.findById(idKupac)
                .orElseThrow(() -> new NijePronadjenoException("Kupac ne postoji."));

        Narudzbina narudzbina = new Narudzbina(kupac);
        narudzbinaRepository.save(narudzbina);

        BigDecimal ukupno = BigDecimal.ZERO;

        for (StavkaZahtev zahtev : stavkeZahtev) {
            if (zahtev.kolicina() <= 0) {
                throw new NevalidniPodaciException("Količina mora biti pozitivna.");
            }
            Igra igra = igraRepository.findById(zahtev.idIgra())
                    .orElseThrow(() -> new NijePronadjenoException("Igra sa id " + zahtev.idIgra() + " ne postoji."));

            if (igra.getKolicinaNaStanju() < zahtev.kolicina()) {
                throw new NevalidniPodaciException("Nema dovoljno igre '" + igra.getNaziv() + "' na stanju.");
            }
            igra.setKolicinaNaStanju(igra.getKolicinaNaStanju() - zahtev.kolicina());

            StavkaNarudzbine stavka = new StavkaNarudzbine(narudzbina, igra, zahtev.kolicina(), igra.getCena());
            narudzbina.getStavke().add(stavka);
            ukupno = ukupno.add(stavka.getUkupno());
        }

        narudzbina.setUkupnaCena(ukupno);
        return narudzbinaRepository.save(narudzbina);
    }

    @Transactional(readOnly = true)
    public List<Narudzbina> narudzbineKupca(Long idKupac) {
        return narudzbinaRepository.findByKupacIdKorisnikOrderByDatumDesc(idKupac);
    }

    @Transactional(readOnly = true)
    public List<Narudzbina> sveNarudzbine() {
        return narudzbinaRepository.findAllByOrderByDatumDesc();
    }

    @Transactional(readOnly = true)
    public Narudzbina nadjiPoId(Long id) {
        return narudzbinaRepository.findById(id)
                .orElseThrow(() -> new NijePronadjenoException("Narudžbina ne postoji."));
    }

    @Transactional
    public Narudzbina promeniStatus(Long id, StatusNarudzbine noviStatus) {
        Narudzbina narudzbina = nadjiPoId(id);
        narudzbina.setStatus(noviStatus);
        return narudzbinaRepository.save(narudzbina);
    }

    @Transactional(readOnly = true)
    public boolean kupacJeKupioIgru(Long idKupac, Long idIgra) {
        return narudzbinaRepository.brojKupovinaIgre(idKupac, idIgra) > 0;
    }
}

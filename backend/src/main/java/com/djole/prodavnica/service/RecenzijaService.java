package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.IgraRepository;
import com.djole.prodavnica.JPARepo.KupacRepository;
import com.djole.prodavnica.JPARepo.NarudzbinaRepository;
import com.djole.prodavnica.JPARepo.RecenzijaRepository;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.model.Recenzija;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecenzijaService {

    private final RecenzijaRepository recenzijaRepository;
    private final NarudzbinaRepository narudzbinaRepository;
    private final KupacRepository kupacRepository;
    private final IgraRepository igraRepository;

    public RecenzijaService(RecenzijaRepository recenzijaRepository, NarudzbinaRepository narudzbinaRepository,
                            KupacRepository kupacRepository, IgraRepository igraRepository) {
        this.recenzijaRepository = recenzijaRepository;
        this.narudzbinaRepository = narudzbinaRepository;
        this.kupacRepository = kupacRepository;
        this.igraRepository = igraRepository;
    }

    @Transactional(readOnly = true)
    public List<Recenzija> recenzijeZaIgru(Long idIgra) {
        return recenzijaRepository.findByIgraIdIgraOrderByDatumDesc(idIgra);
    }

    @Transactional(readOnly = true)
    public Double prosecnaOcena(Long idIgra) {
        return recenzijaRepository.prosecnaOcena(idIgra);
    }

    @Transactional
    public Recenzija ostaviRecenziju(Long idKupac, Long idIgra, int ocena, String komentar) {
        if (ocena < 1 || ocena > 5) {
            throw new NevalidniPodaciException("Ocena mora biti u opsegu 1-5.");
        }
        if (narudzbinaRepository.brojKupovinaIgre(idKupac, idIgra) == 0) {
            throw new NedozvoljenAkcijaException("Recenziju možete ostaviti samo za igru koju ste kupili.");
        }
        if (recenzijaRepository.findByKupacIdKorisnikAndIgraIdIgra(idKupac, idIgra) != null) {
            throw new NevalidniPodaciException("Već ste ostavili recenziju za ovu igru.");
        }
        Kupac kupac = kupacRepository.findById(idKupac)
                .orElseThrow(() -> new NijePronadjenoException("Kupac ne postoji."));
        Igra igra = igraRepository.findById(idIgra)
                .orElseThrow(() -> new NijePronadjenoException("Igra ne postoji."));

        return recenzijaRepository.save(new Recenzija(ocena, komentar, kupac, igra));
    }

    @Transactional
    public Recenzija izmeniRecenziju(Long idRecenzija, Long idKupac, int ocena, String komentar) {
        Recenzija recenzija = nadjiPoId(idRecenzija);
        if (!recenzija.getKupac().getIdKorisnik().equals(idKupac)) {
            throw new NedozvoljenAkcijaException("Možete izmeniti samo sopstvenu recenziju.");
        }
        if (ocena < 1 || ocena > 5) {
            throw new NevalidniPodaciException("Ocena mora biti u opsegu 1-5.");
        }
        recenzija.setOcena(ocena);
        recenzija.setKomentar(komentar);
        return recenzijaRepository.save(recenzija);
    }

    @Transactional
    public void obrisiRecenziju(Long idRecenzija, Long idKupac) {
        Recenzija recenzija = nadjiPoId(idRecenzija);
        if (!recenzija.getKupac().getIdKorisnik().equals(idKupac)) {
            throw new NedozvoljenAkcijaException("Možete obrisati samo sopstvenu recenziju.");
        }
        recenzijaRepository.delete(recenzija);
    }

    @Transactional(readOnly = true)
    public Recenzija nadjiPoId(Long idRecenzija) {
        return recenzijaRepository.findById(idRecenzija)
                .orElseThrow(() -> new NijePronadjenoException("Recenzija ne postoji."));
    }
}

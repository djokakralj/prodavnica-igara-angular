package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.IgraRepository;
import com.djole.prodavnica.JPARepo.IzdavacRepository;
import com.djole.prodavnica.JPARepo.PlatformaRepository;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.model.IgraPlatforma;
import com.djole.prodavnica.model.Izdavac;
import com.djole.prodavnica.model.Platforma;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IgraService {

    public static final Set<String> DOZVOLJENA_SORTIRANJA =
            Set.of("naziv", "cena", "godinaIzdanja", "kolicinaNaStanju");

    public static final int PODRAZUMEVANA_VELICINA_STRANE = 6;

    private static final int NAJVECA_VELICINA_STRANE = 100;

    private final IgraRepository igraRepository;
    private final IzdavacRepository izdavacRepository;
    private final PlatformaRepository platformaRepository;

    public IgraService(IgraRepository igraRepository, IzdavacRepository izdavacRepository,
                       PlatformaRepository platformaRepository) {
        this.igraRepository = igraRepository;
        this.izdavacRepository = izdavacRepository;
        this.platformaRepository = platformaRepository;
    }

    @Transactional(readOnly = true)
    public List<Igra> sveIgre() {
        return igraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Igra> pretraziIgre(String naziv, String zanr, Long idPlatforma, BigDecimal minCena, BigDecimal maxCena) {
        return igraRepository.pretraziIgre(
                (naziv == null || naziv.isBlank()) ? null : naziv,
                (zanr == null || zanr.isBlank()) ? null : zanr,
                idPlatforma, minCena, maxCena);
    }

    @Transactional(readOnly = true)
    public Page<Igra> pretraziIgre(String naziv, String zanr, Long idPlatforma, BigDecimal minCena,
                                   BigDecimal maxCena, String sort, String smer, Integer strana, Integer velicina) {
        int brojStrane = (strana == null) ? 0 : strana;
        int velicinaStrane = (velicina == null) ? PODRAZUMEVANA_VELICINA_STRANE : velicina;

        if (brojStrane < 0) {
            throw new NevalidniPodaciException("Broj strane ne može biti negativan.");
        }
        if (velicinaStrane < 1 || velicinaStrane > NAJVECA_VELICINA_STRANE) {
            throw new NevalidniPodaciException("Veličina strane mora biti između 1 i " + NAJVECA_VELICINA_STRANE + ".");
        }

        String polje = (sort == null || sort.isBlank()) ? "naziv" : sort;
        if (!DOZVOLJENA_SORTIRANJA.contains(polje)) {
            throw new NevalidniPodaciException("Sortiranje po polju '" + polje + "' nije dozvoljeno.");
        }
        Sort.Direction pravac = "opadajuce".equalsIgnoreCase(smer) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return igraRepository.pretraziIgre(
                (naziv == null || naziv.isBlank()) ? null : naziv,
                (zanr == null || zanr.isBlank()) ? null : zanr,
                idPlatforma, minCena, maxCena,
                PageRequest.of(brojStrane, velicinaStrane, Sort.by(pravac, polje)));
    }

    @Transactional(readOnly = true)
    public Igra nadjiPoId(Long id) {
        return igraRepository.findById(id)
                .orElseThrow(() -> new NijePronadjenoException("Igra ne postoji."));
    }
    
    @Transactional(readOnly = true)
    public Map<Long, Igra> nadjiPoIdevima(Collection<Long> idevi) {
        if (idevi == null || idevi.isEmpty()) {
            return Map.of();
        }
        Map<Long, Igra> pronadjene = new HashMap<>();
        for (Igra igra : igraRepository.findAllById(idevi)) {
            pronadjene.put(igra.getIdIgra(), igra);
        }
        return pronadjene;
    }

    @Transactional
    public Igra kreiraj(String naziv, String opis, BigDecimal cena, Integer godinaIzdanja, String zanr,
                        int kolicinaNaStanju, Long idIzdavac) {
        validirajIgru(naziv, cena, kolicinaNaStanju);
        Izdavac izdavac = ucitajIzdavaca(idIzdavac);
        Igra igra = new Igra(naziv, opis, cena, godinaIzdanja, zanr, kolicinaNaStanju, izdavac);
        return igraRepository.save(igra);
    }

    @Transactional
    public Igra izmeni(Long id, String naziv, String opis, BigDecimal cena, Integer godinaIzdanja, String zanr,
                       int kolicinaNaStanju, Long idIzdavac) {
        Igra igra = nadjiPoId(id);
        validirajIgru(naziv, cena, kolicinaNaStanju);
        igra.setNaziv(naziv);
        igra.setOpis(opis);
        igra.setCena(cena);
        igra.setGodinaIzdanja(godinaIzdanja);
        igra.setZanr(zanr);
        igra.setKolicinaNaStanju(kolicinaNaStanju);
        igra.setIzdavac(ucitajIzdavaca(idIzdavac));
        return igraRepository.save(igra);
    }

    @Transactional
    public void obrisi(Long id) {
        igraRepository.delete(nadjiPoId(id));
    }

    @Transactional
    public IgraPlatforma dodajNaPlatformu(Long idIgra, Long idPlatforma, LocalDate datumPortovanja, boolean ekskluzivno) {
        Igra igra = nadjiPoId(idIgra);
        Platforma platforma = platformaRepository.findById(idPlatforma)
                .orElseThrow(() -> new NijePronadjenoException("Platforma ne postoji."));

        boolean vecPostoji = igra.getPlatforme().stream()
                .anyMatch(ip -> ip.getPlatforma().getIdPlatforma().equals(idPlatforma));
        if (vecPostoji) {
            throw new NevalidniPodaciException("Igra je već na toj platformi.");
        }

        IgraPlatforma igraPlatforma = new IgraPlatforma(igra, platforma, datumPortovanja, ekskluzivno);
        igra.getPlatforme().add(igraPlatforma);
        igraRepository.save(igra);
        return igraPlatforma;
    }

    @Transactional
    public void ukloniSaPlatforme(Long idIgra, Long idPlatforma) {
        Igra igra = nadjiPoId(idIgra);
        igra.getPlatforme().removeIf(ip -> ip.getPlatforma().getIdPlatforma().equals(idPlatforma));
        igraRepository.save(igra);
    }

    private Izdavac ucitajIzdavaca(Long idIzdavac) {
        if (idIzdavac == null) {
            return null;
        }
        return izdavacRepository.findById(idIzdavac)
                .orElseThrow(() -> new NijePronadjenoException("Izdavač ne postoji."));
    }

    private void validirajIgru(String naziv, BigDecimal cena, int kolicinaNaStanju) {
        if (naziv == null || naziv.isBlank()) {
            throw new NevalidniPodaciException("Naziv igre je obavezan.");
        }
        if (cena == null || cena.compareTo(BigDecimal.ZERO) < 0) {
            throw new NevalidniPodaciException("Cena mora biti pozitivan broj.");
        }
        if (kolicinaNaStanju < 0) {
            throw new NevalidniPodaciException("Količina na stanju ne može biti negativna.");
        }
    }
}

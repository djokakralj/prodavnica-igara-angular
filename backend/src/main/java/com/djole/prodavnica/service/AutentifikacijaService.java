package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.AdministratorRepository;
import com.djole.prodavnica.JPARepo.KorisnikRepository;
import com.djole.prodavnica.JPARepo.KupacRepository;
import com.djole.prodavnica.model.Administrator;
import com.djole.prodavnica.model.Korisnik;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.service.exception.NedozvoljenAkcijaException;
import com.djole.prodavnica.service.exception.NevalidniPodaciException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutentifikacijaService {

    private final KorisnikRepository korisnikRepository;
    private final KupacRepository kupacRepository;
    private final AdministratorRepository administratorRepository;

    public AutentifikacijaService(KorisnikRepository korisnikRepository, KupacRepository kupacRepository,
                                  AdministratorRepository administratorRepository) {
        this.korisnikRepository = korisnikRepository;
        this.kupacRepository = kupacRepository;
        this.administratorRepository = administratorRepository;
    }

    @Transactional
    public Administrator kreirajAdministratora(String korisnickoIme, String lozinka, String email, int nivoPristupa) {
        return administratorRepository.save(new Administrator(korisnickoIme, lozinka, email, nivoPristupa));
    }

    @Transactional
    public Kupac registrujKupca(String korisnickoIme, String lozinka, String email, String adresa, String brojTelefona) {
        if (korisnickoIme == null || korisnickoIme.isBlank() || lozinka == null || lozinka.length() < 6 || email == null || email.isBlank()) {
            throw new NevalidniPodaciException("Korisničko ime, email i lozinka (min. 6 karaktera) su obavezni.");
        }
        if (korisnikRepository.existsByKorisnickoIme(korisnickoIme)) {
            throw new NevalidniPodaciException("Korisničko ime je zauzeto.");
        }
        if (korisnikRepository.existsByEmail(email)) {
            throw new NevalidniPodaciException("Email je već registrovan.");
        }
        return kupacRepository.save(new Kupac(korisnickoIme, lozinka, email, adresa, brojTelefona));
    }

    @Transactional(readOnly = true)
    public Korisnik prijaviSe(String korisnickoIme, String lozinka) {
        Korisnik korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme);
        if (korisnik == null || !korisnik.getLozinka().equals(lozinka)) {
            throw new NevalidniPodaciException("Pogrešno korisničko ime ili lozinka.");
        }
        if (korisnik instanceof Kupac kupac && kupac.isBlokiran()) {
            throw new NedozvoljenAkcijaException("Nalog je blokiran.");
        }
        return korisnik;
    }
}

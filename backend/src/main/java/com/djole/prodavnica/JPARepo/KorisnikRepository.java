package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    Korisnik findByKorisnickoIme(String korisnickoIme);

    boolean existsByKorisnickoIme(String korisnickoIme);

    boolean existsByEmail(String email);
}

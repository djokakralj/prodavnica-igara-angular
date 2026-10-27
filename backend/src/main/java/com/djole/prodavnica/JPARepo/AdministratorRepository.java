package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Administrator findByKorisnickoIme(String korisnickoIme);
}

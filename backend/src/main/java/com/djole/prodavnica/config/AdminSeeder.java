package com.djole.prodavnica.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.djole.prodavnica.JPARepo.AdministratorRepository;
import com.djole.prodavnica.service.AutentifikacijaService;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final AdministratorRepository administratorRepository;
    private final AutentifikacijaService autentifikacijaService;

    public AdminSeeder(AdministratorRepository administratorRepository, AutentifikacijaService autentifikacijaService) {
        this.administratorRepository = administratorRepository;
        this.autentifikacijaService = autentifikacijaService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (administratorRepository.count() > 0) {
            return;
        }
        String korisnickoIme = env("ADMIN_USERNAME", "admin");
        String lozinka = env("ADMIN_PASSWORD", "admin123");
        String email = env("ADMIN_EMAIL", "admin@example.com");

        autentifikacijaService.kreirajAdministratora(korisnickoIme, lozinka, email, 1);
        log.info("Kreiran podrazumevani administrator '{}'. Promenite lozinku nakon prve prijave!", korisnickoIme);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}

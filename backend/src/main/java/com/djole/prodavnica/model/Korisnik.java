package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Korisnik implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_korisnik")
    private Long idKorisnik;

    @Column(name = "korisnicko_ime", nullable = false, unique = true, length = 50)
    private String korisnickoIme;

    @Column(name = "lozinka", nullable = false)
    private String lozinka;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    protected Korisnik() {
    }

    protected Korisnik(String korisnickoIme, String lozinka, String email) {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.email = email;
    }

    public Long getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(Long idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.djole.prodavnica.dto;

import java.io.Serializable;

public class PrijavaDTO implements DomainDTO, Serializable {

    private String korisnickoIme;
    private String lozinka;

    public PrijavaDTO() {
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
}

package com.djole.prodavnica.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class KorisnikDTO implements DomainDTO, Serializable {

    private Long id;
    private String korisnickoIme;
    private String lozinka;
    private String email;
    private String uloga;
    private String adresa;
    private String brojTelefona;
    private Boolean blokiran;
    private Integer nivoPristupa;

    public KorisnikDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public Boolean getBlokiran() {
        return blokiran;
    }

    public void setBlokiran(Boolean blokiran) {
        this.blokiran = blokiran;
    }

    public Integer getNivoPristupa() {
        return nivoPristupa;
    }

    public void setNivoPristupa(Integer nivoPristupa) {
        this.nivoPristupa = nivoPristupa;
    }

    @Override
    public String toString() {
        return "KorisnikDTO{id=" + id + ", korisnickoIme='" + korisnickoIme + "', uloga='" + uloga + "'}";
    }
}

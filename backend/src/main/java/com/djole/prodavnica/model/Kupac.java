package com.djole.prodavnica.model;

import jakarta.persistence.*;

@Entity
@Table(name = "kupac")
@PrimaryKeyJoinColumn(name = "id_korisnik")
public class Kupac extends Korisnik {

    @Column(name = "adresa")
    private String adresa;

    @Column(name = "broj_telefona", length = 30)
    private String brojTelefona;

    @Column(name = "blokiran", nullable = false)
    private boolean blokiran = false;

    public Kupac() {
        super();
    }

    public Kupac(String korisnickoIme, String lozinka, String email, String adresa, String brojTelefona) {
        super(korisnickoIme, lozinka, email);
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
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

    public boolean isBlokiran() {
        return blokiran;
    }

    public void setBlokiran(boolean blokiran) {
        this.blokiran = blokiran;
    }
}

package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class KorpaStavkaDTO implements DomainDTO, Serializable {

    private Long idIgra;
    private String naziv;
    private BigDecimal cena;
    private int kolicina;
    private BigDecimal ukupno;
    private int kolicinaNaStanju;

    public KorpaStavkaDTO() {
    }

    public KorpaStavkaDTO(Long idIgra, String naziv, BigDecimal cena, int kolicina, BigDecimal ukupno, int kolicinaNaStanju) {
        this.idIgra = idIgra;
        this.naziv = naziv;
        this.cena = cena;
        this.kolicina = kolicina;
        this.ukupno = ukupno;
        this.kolicinaNaStanju = kolicinaNaStanju;
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getUkupno() {
        return ukupno;
    }

    public void setUkupno(BigDecimal ukupno) {
        this.ukupno = ukupno;
    }

    public int getKolicinaNaStanju() {
        return kolicinaNaStanju;
    }

    public void setKolicinaNaStanju(int kolicinaNaStanju) {
        this.kolicinaNaStanju = kolicinaNaStanju;
    }
}

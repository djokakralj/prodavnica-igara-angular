package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IgraDTO implements DomainDTO, Serializable {

    private Long id;
    private String naziv;
    private String opis;
    private BigDecimal cena;
    private Integer godinaIzdanja;
    private String zanr;
    private int kolicinaNaStanju;
    private IzdavacDTO izdavac;
    private List<IgraPlatformaDTO> platforme = new ArrayList<>();
    private Double prosecnaOcena;

    public IgraDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Integer getGodinaIzdanja() {
        return godinaIzdanja;
    }

    public void setGodinaIzdanja(Integer godinaIzdanja) {
        this.godinaIzdanja = godinaIzdanja;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public int getKolicinaNaStanju() {
        return kolicinaNaStanju;
    }

    public void setKolicinaNaStanju(int kolicinaNaStanju) {
        this.kolicinaNaStanju = kolicinaNaStanju;
    }

    public IzdavacDTO getIzdavac() {
        return izdavac;
    }

    public void setIzdavac(IzdavacDTO izdavac) {
        this.izdavac = izdavac;
    }

    public List<IgraPlatformaDTO> getPlatforme() {
        return platforme;
    }

    public void setPlatforme(List<IgraPlatformaDTO> platforme) {
        this.platforme = platforme;
    }

    public Double getProsecnaOcena() {
        return prosecnaOcena;
    }

    public void setProsecnaOcena(Double prosecnaOcena) {
        this.prosecnaOcena = prosecnaOcena;
    }

    @Override
    public String toString() {
        return "IgraDTO{id=" + id + ", naziv='" + naziv + "', cena=" + cena + "}";
    }
}

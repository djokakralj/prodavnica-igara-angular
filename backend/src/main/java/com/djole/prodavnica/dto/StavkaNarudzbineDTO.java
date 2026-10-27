package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StavkaNarudzbineDTO implements DomainDTO, Serializable {

    private Long idIgra;
    private String nazivIgre;
    private int kolicina;
    private BigDecimal cenaPoKomadu;
    private BigDecimal ukupno;

    public StavkaNarudzbineDTO() {
    }

    public StavkaNarudzbineDTO(Long idIgra, String nazivIgre, int kolicina, BigDecimal cenaPoKomadu, BigDecimal ukupno) {
        this.idIgra = idIgra;
        this.nazivIgre = nazivIgre;
        this.kolicina = kolicina;
        this.cenaPoKomadu = cenaPoKomadu;
        this.ukupno = ukupno;
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    public String getNazivIgre() {
        return nazivIgre;
    }

    public void setNazivIgre(String nazivIgre) {
        this.nazivIgre = nazivIgre;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getCenaPoKomadu() {
        return cenaPoKomadu;
    }

    public void setCenaPoKomadu(BigDecimal cenaPoKomadu) {
        this.cenaPoKomadu = cenaPoKomadu;
    }

    public BigDecimal getUkupno() {
        return ukupno;
    }

    public void setUkupno(BigDecimal ukupno) {
        this.ukupno = ukupno;
    }
}

package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NarudzbinaDTO implements DomainDTO, Serializable {

    private Long id;
    private LocalDateTime datum;
    private String status;
    private BigDecimal ukupnaCena;
    private Long idKupac;
    private String korisnickoImeKupca;
    private List<StavkaNarudzbineDTO> stavke = new ArrayList<>();

    public NarudzbinaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Long getIdKupac() {
        return idKupac;
    }

    public void setIdKupac(Long idKupac) {
        this.idKupac = idKupac;
    }

    public String getKorisnickoImeKupca() {
        return korisnickoImeKupca;
    }

    public void setKorisnickoImeKupca(String korisnickoImeKupca) {
        this.korisnickoImeKupca = korisnickoImeKupca;
    }

    public List<StavkaNarudzbineDTO> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaNarudzbineDTO> stavke) {
        this.stavke = stavke;
    }

    @Override
    public String toString() {
        return "NarudzbinaDTO{id=" + id + ", status='" + status + "', ukupnaCena=" + ukupnaCena + "}";
    }
}

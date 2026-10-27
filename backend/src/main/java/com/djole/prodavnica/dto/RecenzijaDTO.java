package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RecenzijaDTO implements DomainDTO, Serializable {

    private Long id;
    private int ocena;
    private String komentar;
    private LocalDateTime datum;
    private Long idKupac;
    private String korisnickoImeKupca;
    private Long idIgra;

    public RecenzijaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
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

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }
}

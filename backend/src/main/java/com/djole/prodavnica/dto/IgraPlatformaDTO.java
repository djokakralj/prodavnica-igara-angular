package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class IgraPlatformaDTO implements DomainDTO, Serializable {

    private Long idPlatforma;
    private String nazivPlatforme;
    private LocalDate datumPortovanja;
    private boolean ekskluzivno;

    public IgraPlatformaDTO() {
    }

    public IgraPlatformaDTO(Long idPlatforma, String nazivPlatforme, LocalDate datumPortovanja, boolean ekskluzivno) {
        this.idPlatforma = idPlatforma;
        this.nazivPlatforme = nazivPlatforme;
        this.datumPortovanja = datumPortovanja;
        this.ekskluzivno = ekskluzivno;
    }

    public Long getIdPlatforma() {
        return idPlatforma;
    }

    public void setIdPlatforma(Long idPlatforma) {
        this.idPlatforma = idPlatforma;
    }

    public String getNazivPlatforme() {
        return nazivPlatforme;
    }

    public void setNazivPlatforme(String nazivPlatforme) {
        this.nazivPlatforme = nazivPlatforme;
    }

    public LocalDate getDatumPortovanja() {
        return datumPortovanja;
    }

    public void setDatumPortovanja(LocalDate datumPortovanja) {
        this.datumPortovanja = datumPortovanja;
    }

    public boolean isEkskluzivno() {
        return ekskluzivno;
    }

    public void setEkskluzivno(boolean ekskluzivno) {
        this.ekskluzivno = ekskluzivno;
    }
}

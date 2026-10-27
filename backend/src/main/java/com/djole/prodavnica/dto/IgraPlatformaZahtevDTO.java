package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class IgraPlatformaZahtevDTO implements DomainDTO, Serializable {

    private Long idIgra;
    private Long idPlatforma;
    private LocalDate datumPortovanja;
    private boolean ekskluzivno;

    public IgraPlatformaZahtevDTO() {
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    public Long getIdPlatforma() {
        return idPlatforma;
    }

    public void setIdPlatforma(Long idPlatforma) {
        this.idPlatforma = idPlatforma;
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

package com.djole.prodavnica.dto;

import java.io.Serializable;

public class StavkaKorpeDTO implements DomainDTO, Serializable {

    private Long idIgra;
    private Integer kolicina;

    public StavkaKorpeDTO() {
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }
}

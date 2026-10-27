package com.djole.prodavnica.dto;

import java.io.Serializable;

public class PlatformaDTO implements DomainDTO, Serializable {

    private Long id;
    private String naziv;

    public PlatformaDTO() {
    }

    public PlatformaDTO(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
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

    @Override
    public String toString() {
        return "PlatformaDTO{id=" + id + ", naziv='" + naziv + "'}";
    }
}

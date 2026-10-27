package com.djole.prodavnica.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StavkaNarudzbineId implements Serializable {

    private Long idNarudzbina;
    private Long idIgra;

    public StavkaNarudzbineId() {
    }

    public StavkaNarudzbineId(Long idNarudzbina, Long idIgra) {
        this.idNarudzbina = idNarudzbina;
        this.idIgra = idIgra;
    }

    public Long getIdNarudzbina() {
        return idNarudzbina;
    }

    public void setIdNarudzbina(Long idNarudzbina) {
        this.idNarudzbina = idNarudzbina;
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StavkaNarudzbineId)) return false;
        StavkaNarudzbineId that = (StavkaNarudzbineId) o;
        return Objects.equals(idNarudzbina, that.idNarudzbina) && Objects.equals(idIgra, that.idIgra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNarudzbina, idIgra);
    }
}

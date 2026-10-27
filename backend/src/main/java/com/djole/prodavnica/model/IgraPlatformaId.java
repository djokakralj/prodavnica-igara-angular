package com.djole.prodavnica.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IgraPlatformaId implements Serializable {

    private Long idIgra;
    private Long idPlatforma;

    public IgraPlatformaId() {
    }

    public IgraPlatformaId(Long idIgra, Long idPlatforma) {
        this.idIgra = idIgra;
        this.idPlatforma = idPlatforma;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IgraPlatformaId)) return false;
        IgraPlatformaId that = (IgraPlatformaId) o;
        return Objects.equals(idIgra, that.idIgra) && Objects.equals(idPlatforma, that.idPlatforma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIgra, idPlatforma);
    }
}

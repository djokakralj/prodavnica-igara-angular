package com.djole.prodavnica.dto;

import java.io.Serializable;

public class BlokirajDTO implements DomainDTO, Serializable {

    private Long id;
    private boolean blokiran;

    public BlokirajDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBlokiran() {
        return blokiran;
    }

    public void setBlokiran(boolean blokiran) {
        this.blokiran = blokiran;
    }
}

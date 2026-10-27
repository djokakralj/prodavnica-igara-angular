package com.djole.prodavnica.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class KorpaDTO implements DomainDTO, Serializable {

    private List<KorpaStavkaDTO> stavke = new ArrayList<>();
    private BigDecimal ukupnaCena = BigDecimal.ZERO;

    public KorpaDTO() {
    }

    public KorpaDTO(List<KorpaStavkaDTO> stavke, BigDecimal ukupnaCena) {
        this.stavke = stavke;
        this.ukupnaCena = ukupnaCena;
    }

    public List<KorpaStavkaDTO> getStavke() {
        return stavke;
    }

    public void setStavke(List<KorpaStavkaDTO> stavke) {
        this.stavke = stavke;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }
}

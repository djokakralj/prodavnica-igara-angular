package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "stavka_narudzbine")
public class StavkaNarudzbine implements Serializable, DomainEntity {

    @EmbeddedId
    private StavkaNarudzbineId id = new StavkaNarudzbineId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idNarudzbina")
    @JoinColumn(name = "id_narudzbina")
    private Narudzbina narudzbina;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIgra")
    @JoinColumn(name = "id_igra")
    private Igra igra;

    @Column(name = "kolicina", nullable = false)
    private int kolicina;

    @Column(name = "cena_po_komadu", nullable = false, precision = 10, scale = 2)
    private BigDecimal cenaPoKomadu;

    public StavkaNarudzbine() {
    }

    public StavkaNarudzbine(Narudzbina narudzbina, Igra igra, int kolicina, BigDecimal cenaPoKomadu) {
        this.narudzbina = narudzbina;
        this.igra = igra;
        this.id = new StavkaNarudzbineId(narudzbina.getIdNarudzbina(), igra.getIdIgra());
        this.kolicina = kolicina;
        this.cenaPoKomadu = cenaPoKomadu;
    }

    public StavkaNarudzbineId getId() {
        return id;
    }

    public void setId(StavkaNarudzbineId id) {
        this.id = id;
    }

    public Narudzbina getNarudzbina() {
        return narudzbina;
    }

    public void setNarudzbina(Narudzbina narudzbina) {
        this.narudzbina = narudzbina;
    }

    public Igra getIgra() {
        return igra;
    }

    public void setIgra(Igra igra) {
        this.igra = igra;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public BigDecimal getCenaPoKomadu() {
        return cenaPoKomadu;
    }

    public void setCenaPoKomadu(BigDecimal cenaPoKomadu) {
        this.cenaPoKomadu = cenaPoKomadu;
    }

    public BigDecimal getUkupno() {
        return cenaPoKomadu.multiply(BigDecimal.valueOf(kolicina));
    }
}

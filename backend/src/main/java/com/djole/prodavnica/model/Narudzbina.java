package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "narudzbina")
public class Narudzbina implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_narudzbina")
    private Long idNarudzbina;

    @Column(name = "datum", nullable = false)
    private LocalDateTime datum;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusNarudzbine status;

    @Column(name = "ukupna_cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal ukupnaCena;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_korisnik", nullable = false)
    private Kupac kupac;

    @OneToMany(mappedBy = "narudzbina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaNarudzbine> stavke = new ArrayList<>();

    public Narudzbina() {
    }

    public Narudzbina(Kupac kupac) {
        this.kupac = kupac;
        this.datum = LocalDateTime.now();
        this.status = StatusNarudzbine.NA_CEKANJU;
        this.ukupnaCena = BigDecimal.ZERO;
    }

    public Long getIdNarudzbina() {
        return idNarudzbina;
    }

    public void setIdNarudzbina(Long idNarudzbina) {
        this.idNarudzbina = idNarudzbina;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public StatusNarudzbine getStatus() {
        return status;
    }

    public void setStatus(StatusNarudzbine status) {
        this.status = status;
    }

    public BigDecimal getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(BigDecimal ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }

    public Kupac getKupac() {
        return kupac;
    }

    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
    }

    public List<StavkaNarudzbine> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaNarudzbine> stavke) {
        this.stavke = stavke;
    }
}

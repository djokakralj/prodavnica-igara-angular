package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "recenzija", uniqueConstraints = @UniqueConstraint(columnNames = {"id_korisnik", "id_igra"}))
public class Recenzija implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recenzija")
    private Long idRecenzija;

    @Column(name = "ocena", nullable = false)
    private int ocena;

    @Column(name = "komentar", columnDefinition = "text")
    private String komentar;

    @Column(name = "datum", nullable = false)
    private LocalDateTime datum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_korisnik", nullable = false)
    private Kupac kupac;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_igra", nullable = false)
    private Igra igra;

    public Recenzija() {
    }

    public Recenzija(int ocena, String komentar, Kupac kupac, Igra igra) {
        this.ocena = ocena;
        this.komentar = komentar;
        this.kupac = kupac;
        this.igra = igra;
        this.datum = LocalDateTime.now();
    }

    public Long getIdRecenzija() {
        return idRecenzija;
    }

    public void setIdRecenzija(Long idRecenzija) {
        this.idRecenzija = idRecenzija;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Kupac getKupac() {
        return kupac;
    }

    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
    }

    public Igra getIgra() {
        return igra;
    }

    public void setIgra(Igra igra) {
        this.igra = igra;
    }
}

package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "igra_platforma")
public class IgraPlatforma implements Serializable, DomainEntity {

    @EmbeddedId
    private IgraPlatformaId id = new IgraPlatformaId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIgra")
    @JoinColumn(name = "id_igra")
    private Igra igra;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPlatforma")
    @JoinColumn(name = "id_platforma")
    private Platforma platforma;

    @Column(name = "datum_portovanja")
    private LocalDate datumPortovanja;

    @Column(name = "ekskluzivno", nullable = false)
    private boolean ekskluzivno;

    public IgraPlatforma() {
    }

    public IgraPlatforma(Igra igra, Platforma platforma, LocalDate datumPortovanja, boolean ekskluzivno) {
        this.igra = igra;
        this.platforma = platforma;
        this.id = new IgraPlatformaId(igra.getIdIgra(), platforma.getIdPlatforma());
        this.datumPortovanja = datumPortovanja;
        this.ekskluzivno = ekskluzivno;
    }

    public IgraPlatformaId getId() {
        return id;
    }

    public void setId(IgraPlatformaId id) {
        this.id = id;
    }

    public Igra getIgra() {
        return igra;
    }

    public void setIgra(Igra igra) {
        this.igra = igra;
    }

    public Platforma getPlatforma() {
        return platforma;
    }

    public void setPlatforma(Platforma platforma) {
        this.platforma = platforma;
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

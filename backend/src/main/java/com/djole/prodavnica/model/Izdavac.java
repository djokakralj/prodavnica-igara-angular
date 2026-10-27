package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "izdavac")
public class Izdavac implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_izdavac")
    private Long idIzdavac;

    @Column(name = "naziv", nullable = false, unique = true)
    private String naziv;

    @Column(name = "drzava")
    private String drzava;

    @OneToMany(mappedBy = "izdavac", orphanRemoval = false)
    private List<Igra> igre = new ArrayList<>();

    public Izdavac() {
    }

    public Izdavac(String naziv, String drzava) {
        this.naziv = naziv;
        this.drzava = drzava;
    }

    public Long getIdIzdavac() {
        return idIzdavac;
    }

    public void setIdIzdavac(Long idIzdavac) {
        this.idIzdavac = idIzdavac;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public List<Igra> getIgre() {
        return igre;
    }

    public void setIgre(List<Igra> igre) {
        this.igre = igre;
    }
}

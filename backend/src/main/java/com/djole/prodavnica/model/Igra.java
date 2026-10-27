package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "igra")
public class Igra implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_igra")
    private Long idIgra;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @Column(name = "opis", columnDefinition = "text")
    private String opis;

    @Column(name = "cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;

    @Column(name = "godina_izdanja")
    private Integer godinaIzdanja;

    @Column(name = "zanr", length = 50)
    private String zanr;

    @Column(name = "kolicina_na_stanju", nullable = false)
    private int kolicinaNaStanju;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_izdavac")
    private Izdavac izdavac;

    @OneToMany(mappedBy = "igra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IgraPlatforma> platforme = new ArrayList<>();

    public Igra() {
    }

    public Igra(String naziv, String opis, BigDecimal cena, Integer godinaIzdanja, String zanr, int kolicinaNaStanju, Izdavac izdavac) {
        this.naziv = naziv;
        this.opis = opis;
        this.cena = cena;
        this.godinaIzdanja = godinaIzdanja;
        this.zanr = zanr;
        this.kolicinaNaStanju = kolicinaNaStanju;
        this.izdavac = izdavac;
    }

    public Long getIdIgra() {
        return idIgra;
    }

    public void setIdIgra(Long idIgra) {
        this.idIgra = idIgra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public Integer getGodinaIzdanja() {
        return godinaIzdanja;
    }

    public void setGodinaIzdanja(Integer godinaIzdanja) {
        this.godinaIzdanja = godinaIzdanja;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public int getKolicinaNaStanju() {
        return kolicinaNaStanju;
    }

    public void setKolicinaNaStanju(int kolicinaNaStanju) {
        this.kolicinaNaStanju = kolicinaNaStanju;
    }

    public Izdavac getIzdavac() {
        return izdavac;
    }

    public void setIzdavac(Izdavac izdavac) {
        this.izdavac = izdavac;
    }

    public List<IgraPlatforma> getPlatforme() {
        return platforme;
    }

    public void setPlatforme(List<IgraPlatforma> platforme) {
        this.platforme = platforme;
    }
}

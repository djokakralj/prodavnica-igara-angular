package com.djole.prodavnica.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "platforma")
public class Platforma implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_platforma")
    private Long idPlatforma;

    @Column(name = "naziv", nullable = false, unique = true)
    private String naziv;

    @OneToMany(mappedBy = "platforma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IgraPlatforma> igrePlatforme = new ArrayList<>();

    public Platforma() {
    }

    public Platforma(String naziv) {
        this.naziv = naziv;
    }

    public Long getIdPlatforma() {
        return idPlatforma;
    }

    public void setIdPlatforma(Long idPlatforma) {
        this.idPlatforma = idPlatforma;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<IgraPlatforma> getIgrePlatforme() {
        return igrePlatforme;
    }

    public void setIgrePlatforme(List<IgraPlatforma> igrePlatforme) {
        this.igrePlatforme = igrePlatforme;
    }
}

package com.djole.prodavnica.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administrator")
@PrimaryKeyJoinColumn(name = "id_korisnik")
public class Administrator extends Korisnik {

    @Column(name = "nivo_pristupa", nullable = false)
    private int nivoPristupa;

    public Administrator() {
        super();
    }

    public Administrator(String korisnickoIme, String lozinka, String email, int nivoPristupa) {
        super(korisnickoIme, lozinka, email);
        this.nivoPristupa = nivoPristupa;
    }

    public int getNivoPristupa() {
        return nivoPristupa;
    }

    public void setNivoPristupa(int nivoPristupa) {
        this.nivoPristupa = nivoPristupa;
    }
}

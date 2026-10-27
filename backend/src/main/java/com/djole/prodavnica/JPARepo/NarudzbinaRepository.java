package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Narudzbina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NarudzbinaRepository extends JpaRepository<Narudzbina, Long> {

    List<Narudzbina> findAllByOrderByDatumDesc();

    List<Narudzbina> findByKupacIdKorisnikOrderByDatumDesc(Long idKupac);

    @Query("SELECT COUNT(s) FROM StavkaNarudzbine s "
            + "WHERE s.narudzbina.kupac.idKorisnik = :idKupac AND s.igra.idIgra = :idIgra")
    long brojKupovinaIgre(@Param("idKupac") Long idKupac, @Param("idIgra") Long idIgra);
}

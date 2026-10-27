package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Recenzija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecenzijaRepository extends JpaRepository<Recenzija, Long> {

    List<Recenzija> findByIgraIdIgraOrderByDatumDesc(Long idIgra);

    Recenzija findByKupacIdKorisnikAndIgraIdIgra(Long idKupac, Long idIgra);

    @Query("SELECT AVG(r.ocena) FROM Recenzija r WHERE r.igra.idIgra = :idIgra")
    Double prosecnaOcena(@Param("idIgra") Long idIgra);
}

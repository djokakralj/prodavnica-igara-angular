package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Kupac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KupacRepository extends JpaRepository<Kupac, Long> {

    @Query("SELECT k FROM Kupac k WHERE (:pretraga IS NULL "
            + "OR LOWER(k.korisnickoIme) LIKE LOWER(CONCAT('%', :pretraga, '%')) "
            + "OR LOWER(k.email) LIKE LOWER(CONCAT('%', :pretraga, '%'))) "
            + "ORDER BY k.korisnickoIme")
    List<Kupac> pretraziKupce(@Param("pretraga") String pretraga);
}

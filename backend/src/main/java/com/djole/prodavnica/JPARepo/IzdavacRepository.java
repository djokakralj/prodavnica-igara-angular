package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Izdavac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IzdavacRepository extends JpaRepository<Izdavac, Long> {

    @Query("SELECT i FROM Izdavac i "
            + "WHERE (:naziv IS NULL OR LOWER(i.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) "
            + "ORDER BY i.naziv")
    List<Izdavac> pretrazi(@Param("naziv") String naziv);

    boolean existsByNazivIgnoreCase(String naziv);

    boolean existsByNazivIgnoreCaseAndIdIzdavacNot(String naziv, Long idIzdavac);
}

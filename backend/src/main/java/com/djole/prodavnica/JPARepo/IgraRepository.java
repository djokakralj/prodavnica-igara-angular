package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Igra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IgraRepository extends JpaRepository<Igra, Long> {

    List<Igra> findByNazivContainingIgnoreCaseOrderByNaziv(String naziv);

    @Query("SELECT DISTINCT i FROM Igra i "
            + "LEFT JOIN i.platforme ip "
            + "LEFT JOIN ip.platforma p "
            + "WHERE (:naziv IS NULL OR LOWER(i.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) "
            + "AND (:zanr IS NULL OR i.zanr = :zanr) "
            + "AND (:minCena IS NULL OR i.cena >= :minCena) "
            + "AND (:maxCena IS NULL OR i.cena <= :maxCena) "
            + "AND (:idPlatforma IS NULL OR p.idPlatforma = :idPlatforma) "
            + "ORDER BY i.naziv")
    List<Igra> pretraziIgre(@Param("naziv") String naziv,
                            @Param("zanr") String zanr,
                            @Param("idPlatforma") Long idPlatforma,
                            @Param("minCena") BigDecimal minCena,
                            @Param("maxCena") BigDecimal maxCena);

    @Query(value = "SELECT DISTINCT i FROM Igra i "
            + "LEFT JOIN i.platforme ip "
            + "LEFT JOIN ip.platforma p "
            + "WHERE (:naziv IS NULL OR LOWER(i.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) "
            + "AND (:zanr IS NULL OR i.zanr = :zanr) "
            + "AND (:minCena IS NULL OR i.cena >= :minCena) "
            + "AND (:maxCena IS NULL OR i.cena <= :maxCena) "
            + "AND (:idPlatforma IS NULL OR p.idPlatforma = :idPlatforma)",
            countQuery = "SELECT COUNT(DISTINCT i) FROM Igra i "
            + "LEFT JOIN i.platforme ip "
            + "LEFT JOIN ip.platforma p "
            + "WHERE (:naziv IS NULL OR LOWER(i.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))) "
            + "AND (:zanr IS NULL OR i.zanr = :zanr) "
            + "AND (:minCena IS NULL OR i.cena >= :minCena) "
            + "AND (:maxCena IS NULL OR i.cena <= :maxCena) "
            + "AND (:idPlatforma IS NULL OR p.idPlatforma = :idPlatforma)")
    Page<Igra> pretraziIgre(@Param("naziv") String naziv,
                            @Param("zanr") String zanr,
                            @Param("idPlatforma") Long idPlatforma,
                            @Param("minCena") BigDecimal minCena,
                            @Param("maxCena") BigDecimal maxCena,
                            Pageable strana);
}

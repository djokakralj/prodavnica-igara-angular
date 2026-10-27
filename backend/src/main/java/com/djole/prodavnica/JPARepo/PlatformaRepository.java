package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.Platforma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformaRepository extends JpaRepository<Platforma, Long> {

    List<Platforma> findAllByOrderByNaziv();

    boolean existsByNazivIgnoreCase(String naziv);

    boolean existsByNazivIgnoreCaseAndIdPlatformaNot(String naziv, Long idPlatforma);
}

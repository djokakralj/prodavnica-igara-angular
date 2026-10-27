package com.djole.prodavnica.JPARepo;

import com.djole.prodavnica.model.IgraPlatforma;
import com.djole.prodavnica.model.IgraPlatformaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IgraPlatformaRepository extends JpaRepository<IgraPlatforma, IgraPlatformaId> {

}

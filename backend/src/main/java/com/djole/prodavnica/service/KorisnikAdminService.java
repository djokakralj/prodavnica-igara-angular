package com.djole.prodavnica.service;

import com.djole.prodavnica.JPARepo.KupacRepository;
import com.djole.prodavnica.model.Kupac;
import com.djole.prodavnica.service.exception.NijePronadjenoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KorisnikAdminService {

    private final KupacRepository kupacRepository;

    public KorisnikAdminService(KupacRepository kupacRepository) {
        this.kupacRepository = kupacRepository;
    }

    @Transactional(readOnly = true)
    public List<Kupac> pretraziKupce(String pretraga) {
        return kupacRepository.pretraziKupce((pretraga == null || pretraga.isBlank()) ? null : pretraga);
    }

    @Transactional
    public Kupac postaviBlokiran(Long idKupac, boolean blokiran) {
        Kupac kupac = kupacRepository.findById(idKupac)
                .orElseThrow(() -> new NijePronadjenoException("Kupac ne postoji."));
        kupac.setBlokiran(blokiran);
        return kupacRepository.save(kupac);
    }
}

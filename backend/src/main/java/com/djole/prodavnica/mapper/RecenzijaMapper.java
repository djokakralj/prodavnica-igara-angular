package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.RecenzijaDTO;
import com.djole.prodavnica.model.Recenzija;
import org.springframework.stereotype.Component;

@Component
public class RecenzijaMapper implements BaseMapper<RecenzijaDTO, Recenzija> {

    @Override
    public RecenzijaDTO toDomainDTO(Recenzija entity) {
        if (entity == null) {
            return null;
        }
        RecenzijaDTO dto = new RecenzijaDTO();
        dto.setId(entity.getIdRecenzija());
        dto.setOcena(entity.getOcena());
        dto.setKomentar(entity.getKomentar());
        dto.setDatum(entity.getDatum());
        if (entity.getKupac() != null) {
            dto.setIdKupac(entity.getKupac().getIdKorisnik());
            dto.setKorisnickoImeKupca(entity.getKupac().getKorisnickoIme());
        }
        if (entity.getIgra() != null) {
            dto.setIdIgra(entity.getIgra().getIdIgra());
        }
        return dto;
    }

    @Override
    public Recenzija toDomainEntity(RecenzijaDTO dto) {
        if (dto == null) {
            return null;
        }
        Recenzija entity = new Recenzija();
        entity.setIdRecenzija(dto.getId());
        entity.setOcena(dto.getOcena());
        entity.setKomentar(dto.getKomentar());
        entity.setDatum(dto.getDatum());
        return entity;
    }
}

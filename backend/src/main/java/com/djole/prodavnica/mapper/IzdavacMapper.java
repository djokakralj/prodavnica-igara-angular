package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.IzdavacDTO;
import com.djole.prodavnica.model.Izdavac;
import org.springframework.stereotype.Component;

@Component
public class IzdavacMapper implements BaseMapper<IzdavacDTO, Izdavac> {

    @Override
    public IzdavacDTO toDomainDTO(Izdavac entity) {
        if (entity == null) {
            return null;
        }
        return new IzdavacDTO(entity.getIdIzdavac(), entity.getNaziv(), entity.getDrzava());
    }

    @Override
    public Izdavac toDomainEntity(IzdavacDTO dto) {
        if (dto == null) {
            return null;
        }
        Izdavac entity = new Izdavac();
        entity.setIdIzdavac(dto.getId());
        entity.setNaziv(dto.getNaziv());
        entity.setDrzava(dto.getDrzava());
        return entity;
    }
}

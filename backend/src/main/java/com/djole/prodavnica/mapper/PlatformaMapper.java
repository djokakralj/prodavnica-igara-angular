package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.PlatformaDTO;
import com.djole.prodavnica.model.Platforma;
import org.springframework.stereotype.Component;

@Component
public class PlatformaMapper implements BaseMapper<PlatformaDTO, Platforma> {

    @Override
    public PlatformaDTO toDomainDTO(Platforma entity) {
        if (entity == null) {
            return null;
        }
        return new PlatformaDTO(entity.getIdPlatforma(), entity.getNaziv());
    }

    @Override
    public Platforma toDomainEntity(PlatformaDTO dto) {
        if (dto == null) {
            return null;
        }
        Platforma entity = new Platforma();
        entity.setIdPlatforma(dto.getId());
        entity.setNaziv(dto.getNaziv());
        return entity;
    }
}

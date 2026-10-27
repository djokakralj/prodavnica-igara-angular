package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.IgraPlatformaDTO;
import com.djole.prodavnica.model.IgraPlatforma;
import org.springframework.stereotype.Component;

@Component
public class IgraPlatformaMapper implements BaseMapper<IgraPlatformaDTO, IgraPlatforma> {

    @Override
    public IgraPlatformaDTO toDomainDTO(IgraPlatforma entity) {
        if (entity == null) {
            return null;
        }
        return new IgraPlatformaDTO(
                entity.getPlatforma().getIdPlatforma(),
                entity.getPlatforma().getNaziv(),
                entity.getDatumPortovanja(),
                entity.isEkskluzivno());
    }

    @Override
    public IgraPlatforma toDomainEntity(IgraPlatformaDTO dto) {
        if (dto == null) {
            return null;
        }
        IgraPlatforma entity = new IgraPlatforma();
        entity.setDatumPortovanja(dto.getDatumPortovanja());
        entity.setEkskluzivno(dto.isEkskluzivno());
        return entity;
    }
}

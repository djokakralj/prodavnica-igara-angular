package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.StavkaNarudzbineDTO;
import com.djole.prodavnica.model.StavkaNarudzbine;
import org.springframework.stereotype.Component;

@Component
public class StavkaNarudzbineMapper implements BaseMapper<StavkaNarudzbineDTO, StavkaNarudzbine> {

    @Override
    public StavkaNarudzbineDTO toDomainDTO(StavkaNarudzbine entity) {
        if (entity == null) {
            return null;
        }
        return new StavkaNarudzbineDTO(
                entity.getIgra().getIdIgra(),
                entity.getIgra().getNaziv(),
                entity.getKolicina(),
                entity.getCenaPoKomadu(),
                entity.getUkupno());
    }

    @Override
    public StavkaNarudzbine toDomainEntity(StavkaNarudzbineDTO dto) {
        if (dto == null) {
            return null;
        }
        StavkaNarudzbine entity = new StavkaNarudzbine();
        entity.setKolicina(dto.getKolicina());
        entity.setCenaPoKomadu(dto.getCenaPoKomadu());
        return entity;
    }
}

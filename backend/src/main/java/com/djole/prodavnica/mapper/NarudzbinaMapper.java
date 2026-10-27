package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.NarudzbinaDTO;
import com.djole.prodavnica.dto.StavkaNarudzbineDTO;
import com.djole.prodavnica.model.Narudzbina;
import com.djole.prodavnica.model.StatusNarudzbine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NarudzbinaMapper implements BaseMapper<NarudzbinaDTO, Narudzbina> {

    private final StavkaNarudzbineMapper stavkaNarudzbineMapper;

    public NarudzbinaMapper(StavkaNarudzbineMapper stavkaNarudzbineMapper) {
        this.stavkaNarudzbineMapper = stavkaNarudzbineMapper;
    }

    @Override
    public NarudzbinaDTO toDomainDTO(Narudzbina entity) {
        if (entity == null) {
            return null;
        }
        NarudzbinaDTO dto = new NarudzbinaDTO();
        dto.setId(entity.getIdNarudzbina());
        dto.setDatum(entity.getDatum());
        dto.setStatus(entity.getStatus() == null ? null : entity.getStatus().name());
        dto.setUkupnaCena(entity.getUkupnaCena());

        if (entity.getKupac() != null) {
            dto.setIdKupac(entity.getKupac().getIdKorisnik());
            dto.setKorisnickoImeKupca(entity.getKupac().getKorisnickoIme());
        }

        List<StavkaNarudzbineDTO> stavke = entity.getStavke().stream()
                .map(stavkaNarudzbineMapper::toDomainDTO)
                .collect(Collectors.toList());
        dto.setStavke(stavke);
        return dto;
    }

    @Override
    public Narudzbina toDomainEntity(NarudzbinaDTO dto) {
        if (dto == null) {
            return null;
        }
        Narudzbina entity = new Narudzbina();
        entity.setIdNarudzbina(dto.getId());
        entity.setDatum(dto.getDatum());
        entity.setUkupnaCena(dto.getUkupnaCena());
        if (dto.getStatus() != null) {
            entity.setStatus(StatusNarudzbine.valueOf(dto.getStatus()));
        }
        return entity;
    }
}

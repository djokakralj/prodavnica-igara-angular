package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.IgraDTO;
import com.djole.prodavnica.model.Igra;
import com.djole.prodavnica.model.Izdavac;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IgraMapper implements BaseMapper<IgraDTO, Igra> {

    private final IzdavacMapper izdavacMapper;
    private final IgraPlatformaMapper igraPlatformaMapper;

    public IgraMapper(IzdavacMapper izdavacMapper, IgraPlatformaMapper igraPlatformaMapper) {
        this.izdavacMapper = izdavacMapper;
        this.igraPlatformaMapper = igraPlatformaMapper;
    }

    @Override
    public IgraDTO toDomainDTO(Igra entity) {
        return toDomainDTO(entity, null);
    }

    public IgraDTO toDomainDTO(Igra entity, Double prosecnaOcena) {
        if (entity == null) {
            return null;
        }
        IgraDTO dto = new IgraDTO();
        dto.setId(entity.getIdIgra());
        dto.setNaziv(entity.getNaziv());
        dto.setOpis(entity.getOpis());
        dto.setCena(entity.getCena());
        dto.setGodinaIzdanja(entity.getGodinaIzdanja());
        dto.setZanr(entity.getZanr());
        dto.setKolicinaNaStanju(entity.getKolicinaNaStanju());
        dto.setIzdavac(izdavacMapper.toDomainDTO(entity.getIzdavac()));

        List<com.djole.prodavnica.dto.IgraPlatformaDTO> platforme = entity.getPlatforme().stream()
                .map(igraPlatformaMapper::toDomainDTO)
                .collect(Collectors.toList());
        dto.setPlatforme(platforme);
        dto.setProsecnaOcena(prosecnaOcena);
        return dto;
    }

    @Override
    public Igra toDomainEntity(IgraDTO dto) {
        if (dto == null) {
            return null;
        }
        Igra entity = new Igra();
        entity.setIdIgra(dto.getId());
        entity.setNaziv(dto.getNaziv());
        entity.setOpis(dto.getOpis());
        entity.setCena(dto.getCena());
        entity.setGodinaIzdanja(dto.getGodinaIzdanja());
        entity.setZanr(dto.getZanr());
        entity.setKolicinaNaStanju(dto.getKolicinaNaStanju());
        return entity;
    }

    public Long idIzdavaca(IgraDTO dto) {
        return (dto == null || dto.getIzdavac() == null) ? null : dto.getIzdavac().getId();
    }
}

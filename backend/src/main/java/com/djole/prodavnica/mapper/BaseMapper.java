package com.djole.prodavnica.mapper;

import com.djole.prodavnica.dto.DomainDTO;
import com.djole.prodavnica.model.DomainEntity;

public interface BaseMapper<DTO extends DomainDTO, DB extends DomainEntity> {

    DTO toDomainDTO(DB domainEntity);

    DB toDomainEntity(DTO domainDTO);
}

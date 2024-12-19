package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.model.Zone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    ZoneResponseDTO toResponseDTO(Zone zone);

    Zone toEntity(ZoneRequestDTO zoneRequestDTO);
}

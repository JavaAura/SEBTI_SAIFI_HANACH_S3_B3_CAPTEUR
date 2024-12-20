package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.DeviceDTO;
import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    @Mapping(target = "devices", source = "devices")
    ZoneResponseDTO toResponseDTO(Zone zone);

    List<DeviceDTO> toDeviceDTOs(List<Device> devices);

    Zone toEntity(ZoneRequestDTO zoneRequestDTO);
}

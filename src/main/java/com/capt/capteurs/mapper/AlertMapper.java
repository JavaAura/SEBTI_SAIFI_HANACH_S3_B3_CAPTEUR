package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.AlertRequestDTO;
import com.capt.capteurs.dto.response.AlertResponseDTO;
import com.capt.capteurs.model.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    @Mapping(source = "deviceId.id", target = "deviceId")
    @Mapping(source = "deviceId.name", target = "deviceName")
    AlertResponseDTO toResponseDTO(Alert alert);

    @Mapping(source = "deviceId", target = "deviceId.id")
    Alert toEntity(AlertRequestDTO alertRequestDTO);
}
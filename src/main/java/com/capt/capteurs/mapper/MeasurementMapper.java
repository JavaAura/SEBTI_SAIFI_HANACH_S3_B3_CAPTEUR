package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;
import com.capt.capteurs.model.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {
    @Mapping(source = "deviceId.id", target = "deviceId")
    @Mapping(source = "deviceId.name", target = "deviceName")
    MeasurementResponseDTO toResponseDTO(Measurement measurement);

    @Mapping(source = "deviceId", target = "deviceId.id")
    Measurement toEntity(MeasurementRequestDTO measurementRequestDTO);
}
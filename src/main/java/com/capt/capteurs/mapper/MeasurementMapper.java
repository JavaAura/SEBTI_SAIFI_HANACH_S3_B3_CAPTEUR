package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.dto.RoleDTO;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {
    Measurement toEntity(MeasurementDTO measurementDTO);

    MeasurementDTO toResponseDTO(Measurement measurement);
}
package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.request.AlertRequestDTO;
import com.capt.capteurs.dto.response.AlertResponseDTO;
import com.capt.capteurs.model.Alert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    Alert toEntity(AlertRequestDTO alertRequestDTO);

    AlertResponseDTO toResponseDTO(Alert alert);
}
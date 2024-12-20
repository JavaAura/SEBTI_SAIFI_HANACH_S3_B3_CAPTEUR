package com.capt.capteurs.service;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasurementService {
    MeasurementResponseDTO save(MeasurementRequestDTO measurementDTO);

    Page<MeasurementResponseDTO> getMeasurementsByDevice(String deviceId, Pageable pageable);

    Page<MeasurementResponseDTO> getAllMeasurements(Pageable pageable);

    byte[] exportMeasurements();
}

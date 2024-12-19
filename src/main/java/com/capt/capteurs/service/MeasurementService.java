package com.capt.capteurs.service;

import com.capt.capteurs.dto.MeasurementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasurementService {
    MeasurementDTO save(MeasurementDTO measurementDTO);

    Page<MeasurementDTO> getMeasurementsByDevice(String deviceId, Pageable pageable);

    Page<MeasurementDTO> getAllMeasurements(Pageable pageable);

    byte[] exportMeasurements();
}

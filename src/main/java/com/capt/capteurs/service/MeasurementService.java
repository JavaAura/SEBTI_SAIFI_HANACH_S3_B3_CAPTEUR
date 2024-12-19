package com.capt.capteurs.service;

import com.capt.capteurs.dto.MeasurementDTO;

import java.util.List;

public interface MeasurementService {
    MeasurementDTO save(MeasurementDTO measurementDTO);
    List<MeasurementDTO> getMeasurementsByDevice(String deviceId);
    List<MeasurementDTO> getAllMeasurements();
}

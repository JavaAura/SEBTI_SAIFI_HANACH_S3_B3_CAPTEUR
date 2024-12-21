package com.capt.capteurs.service;

import com.capt.capteurs.dto.response.AlertResponseDTO;

import java.util.List;

public interface AlertService {
    List<AlertResponseDTO> getAllAlerts();
    List<AlertResponseDTO> getAlertsByDevice(String deviceId);
    List<AlertResponseDTO> getAlertsBySeverity(String severity);
    AlertResponseDTO createAlertFromMeasurement(String deviceId, double value, String deviceType);
}

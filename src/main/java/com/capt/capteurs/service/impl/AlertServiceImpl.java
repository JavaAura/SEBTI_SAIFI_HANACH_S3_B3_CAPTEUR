package com.capt.capteurs.service.impl;

import com.capt.capteurs.dto.response.AlertResponseDTO;
import com.capt.capteurs.mapper.AlertMapper;
import com.capt.capteurs.model.Alert;
import com.capt.capteurs.repository.AlertRepository;
import com.capt.capteurs.service.AlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public AlertServiceImpl(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    @Override
    public List<AlertResponseDTO> getAllAlerts() {
        return alertRepository.findAll()
                .stream()
                .map(alertMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponseDTO> getAlertsByDevice(String deviceId) {
        return alertRepository.findByDeviceId(deviceId)
                .stream()
                .map(alertMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlertResponseDTO> getAlertsBySeverity(String severity) {
        return alertRepository.findBySeverity(severity)
                .stream()
                .map(alertMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AlertResponseDTO createAlertFromMeasurement(String deviceId, double value, String deviceType) {
        String severity;
        String message;

        if ("temperature".equalsIgnoreCase(deviceType)) {
            if (value > 40 || value < -10) {
                severity = "CRITICAL";
                message = "Risque immédiat pour les équipements";
            } else if ((value > 35 && value <= 40) || (value >= -10 && value <= -5)) {
                severity = "HIGH";
                message = "Situation préoccupante nécessitant une action rapide";
            } else if ((value > 30 && value <= 35) || (value >= -5 && value <= 0)) {
                severity = "MEDIUM";
                message = "Situation à surveiller";
            } else if ((value > 25 && value <= 30)) {
                severity = "LOW";
                message = "Légère déviation des valeurs optimales";
            } else {
                severity = "NORMAL";
                message = "Température dans la plage optimale";
            }
        } else if ("humidity".equalsIgnoreCase(deviceType)) {
            if (value > 90 || value < 20) {
                severity = "CRITICAL";
                message = "Risque de dommages matériels";
            } else if ((value > 80 && value <= 90) || (value >= 20 && value <= 30)) {
                severity = "HIGH";
                message = "Conditions défavorables";
            } else if ((value > 70 && value <= 80) || (value >= 30 && value <= 40)) {
                severity = "MEDIUM";
                message = "Situation à surveiller";
            } else if ((value > 65 && value <= 70) || (value >= 40 && value <= 45)) {
                severity = "LOW";
                message = "Légère déviation";
            } else {
                severity = "NORMAL";
                message = "Humidité dans la plage optimale";
            }
        } else {
            throw new IllegalArgumentException("Type d'appareil non supporté : " + deviceType);
        }

        Alert alert = new Alert();
        alert.setDeviceId(deviceId);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setTimestamp(LocalDateTime.now());

        Alert savedAlert = alertRepository.save(alert);
        return alertMapper.toResponseDTO(savedAlert);
    }
}
package com.capt.capteurs.service.impl;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.AlertService;
import com.capt.capteurs.service.MeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final MeasurementMapper measurementMapper;
    private final AlertService  alertService;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, DeviceRepository deviceRepository, MeasurementMapper measurementMapper , AlertService alertService) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
        this.measurementMapper = measurementMapper;
        this.alertService = alertService;
    }

    @Override
    public Page<MeasurementResponseDTO> getAllMeasurements(Pageable pageable) {
        Page<Measurement> measurements = measurementRepository.findAll(pageable);
        return measurements.map(measurementMapper::toResponseDTO);
    }

    @Override
    public Page<MeasurementResponseDTO> getMeasurementsByDevice(String deviceId , Pageable pageable) {
        List<Measurement> measurements = measurementRepository.findByDeviceId(deviceId,pageable);
        return new PageImpl<>(
                measurements.stream().map(measurementMapper::toResponseDTO).toList(),
                pageable,
                measurements.size()
        );
    }

    @Override
    public MeasurementResponseDTO save(MeasurementRequestDTO measurementDTO) {
        Device device = deviceRepository.findById(measurementDTO.getDeviceId())
            .orElseThrow(() -> new IllegalArgumentException("Device introuvable : " + measurementDTO.getDeviceId()));
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement.setTimestamp(measurementDTO.getTimestamp() == null ? LocalDateTime.now() : measurementDTO.getTimestamp());
        measurement.setDeviceId(device);
        Measurement savedMeasurement = measurementRepository.save(measurement);

        String deviceId = measurementDTO.getDeviceId();
        String deviceName = measurementDTO.getDeviceName();
        double value = measurementDTO.getValue();
        alertService.createAlertFromMeasurement(deviceId,value,deviceName);
        return measurementMapper.toResponseDTO(savedMeasurement);
    }

    @Override
    public byte[] exportMeasurements(){
        List<Measurement> measurements = measurementRepository.findAll();
        StringBuilder csvData = new StringBuilder();

        // Ajouter les en-têtes du CSV
        csvData.append("id,timestamp,value,deviceId\n");

        // Ajouter les données des mesures
        for (Measurement measurement : measurements) {
            csvData.append(measurement.getId())
                    .append(",")
                    .append(measurement.getTimestamp())
                    .append(",")
                    .append(measurement.getValue())
                    .append(",")
                    .append(measurement.getDeviceId())
                    .append("\n");
        }

        return csvData.toString().getBytes(StandardCharsets.UTF_8);
    }
}

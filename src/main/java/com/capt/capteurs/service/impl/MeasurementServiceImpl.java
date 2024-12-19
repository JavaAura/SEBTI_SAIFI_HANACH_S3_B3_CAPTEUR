package com.capt.capteurs.service.impl;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.MeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    @Override
    public Page<MeasurementDTO> getAllMeasurements(Pageable pageable) {
        Page<Measurement> measurements = measurementRepository.findAll(pageable);
        return measurements.map(measurementMapper::toResponseDTO);
    }

    @Override
    public Page<MeasurementDTO> getMeasurementsByDevice(String deviceId , Pageable pageable) {
        List<Measurement> measurements = measurementRepository.findByDeviceId(deviceId,pageable);
        return new PageImpl<>(
                measurements.stream().map(measurementMapper::toResponseDTO).toList(),
                pageable,
                measurements.size()
        );
    }

    @Override
    public MeasurementDTO save(MeasurementDTO measurementDTO) {
        Measurement measurement = measurementMapper.toEntity(measurementDTO);
        measurement.setTimestamp(measurementDTO.getTimestamp() == null ? LocalDateTime.now() : measurementDTO.getTimestamp());
        Measurement savedMeasurement = measurementRepository.save(measurement);
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

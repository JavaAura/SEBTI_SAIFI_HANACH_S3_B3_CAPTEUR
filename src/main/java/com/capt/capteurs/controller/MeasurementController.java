package com.capt.capteurs.controller;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.service.MeasurementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<MeasurementDTO> addMeasurement(@RequestBody MeasurementDTO measurement) {
        MeasurementDTO createdMeasurement = measurementService.save(measurement);
        return new ResponseEntity<>(createdMeasurement, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Page<MeasurementDTO>> getAllMeasurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasurementDTO> measurements = measurementService.getAllMeasurements(pageable);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Page<MeasurementDTO>> getMeasurementsByDevice(@PathVariable String deviceId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeasurementDTO> measurements = measurementService.getMeasurementsByDevice(deviceId,pageable);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<byte[]> exportMeasurements() {
        byte[] csvData = measurementService.exportMeasurements();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=measurements.csv");
        return ResponseEntity.ok().headers(headers).body(csvData);
    }
}
package com.capt.capteurs.controller;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.service.MeasurementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping
    public ResponseEntity<MeasurementDTO> addMeasurement(@RequestBody MeasurementDTO measurement) {
        MeasurementDTO createdMeasurement = measurementService.save(measurement);
        return new ResponseEntity<>(createdMeasurement, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<MeasurementDTO> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<MeasurementDTO>> getMeasurementsByDevice(@PathVariable String deviceId) {
        List<MeasurementDTO> measurements = measurementService.getMeasurementsByDevice(deviceId);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportMeasurements() {
        byte[] csvData = measurementService.exportMeasurements();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=measurements.csv");
        return ResponseEntity.ok().headers(headers).body(csvData);
    }
}

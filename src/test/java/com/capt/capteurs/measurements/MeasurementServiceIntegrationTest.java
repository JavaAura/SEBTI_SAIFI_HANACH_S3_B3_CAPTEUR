package com.capt.capteurs.measurements;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.DeviceType;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeasurementServiceIntegrationTest {

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        deviceRepository.deleteAll();
    }


    @Test
    void testGetAllMeasurementsIntegration() {
        // Arrange
        Device device = new Device();
        device.setId("device123");
        device.setName("Test Device");
        deviceRepository.save(device);

        Measurement measurement = new Measurement();
        measurement.setValue(30.5);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setDeviceId(device);
        measurementRepository.save(measurement);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<MeasurementResponseDTO> measurements = measurementService.getAllMeasurements(pageable);

        // Assert
        assertNotNull(measurements);
        assertEquals(1, measurements.getTotalElements());
    }

    @Test
    void testGetMeasurementsByDeviceIntegration_Success() {
        // Arrange
        Device device = new Device();
        device.setId("device123");
        device.setName("Test Device");
        deviceRepository.save(device);

        Measurement measurement = new Measurement();
        measurement.setValue(55.5);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setDeviceId(device);
        measurementRepository.save(measurement);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<MeasurementResponseDTO> responseDTOs = measurementService.getMeasurementsByDevice("device123", pageable);

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.getContent().size());
    }

    @Test
    void testSaveMeasurementIntegration_DeviceNotFound() {
        // Arrange
        MeasurementRequestDTO requestDTO = MeasurementRequestDTO.builder()
                .deviceId("invalidDevice")
                .value(22.0)
                .timestamp(LocalDateTime.now())
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> measurementService.save(requestDTO));
        assertEquals("Device introuvable : invalidDevice", exception.getMessage());
    }

    @Test
    void testExportMeasurementsIntegration() {
        // Arrange
        Device device = new Device();
        device.setId("device123");
        device.setName("Export Test Device");
        deviceRepository.save(device);

        Measurement measurement = new Measurement();
        measurement.setValue(65.5);
        measurement.setTimestamp(LocalDateTime.now());
        measurement.setDeviceId(device);
        measurementRepository.save(measurement);

        // Act
        byte[] csvData = measurementService.exportMeasurements();

        // Assert
        assertNotNull(csvData);
        String csvContent = new String(csvData);
        assertTrue(csvContent.contains("id,timestamp,value"));
        assertTrue(csvContent.contains("65.5"));
    }
}

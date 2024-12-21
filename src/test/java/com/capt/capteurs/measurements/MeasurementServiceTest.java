package com.capt.capteurs.measurements;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.AlertService;
import com.capt.capteurs.service.impl.MeasurementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementServiceTest {

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private MeasurementMapper measurementMapper;

    @Mock
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMeasurements_NoMeasurements() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(measurementRepository.findAll(pageable)).thenReturn(Page.empty());

        // Act
        Page<MeasurementResponseDTO> result = measurementService.getAllMeasurements(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result page should be empty");
        verify(measurementRepository, times(1)).findAll(pageable);
        verifyNoInteractions(measurementMapper);
    }

    @Test
    void testGetMeasurementsByDevice_Success() {
        // Arrange
        Device device = Device.builder().build();
        device.setId("123");
        Pageable pageable = PageRequest.of(0, 10);

        Measurement measurement = Measurement.builder().id("1").value(25.5).timestamp(LocalDateTime.now()).deviceId(device).build();
        MeasurementResponseDTO dto = MeasurementResponseDTO.builder().id("1").value(25.5).build();

        when(measurementRepository.findByDeviceId(device.getId(), pageable)).thenReturn(List.of(measurement));
        when(measurementMapper.toResponseDTO(measurement)).thenReturn(dto);

        // Act
        Page<MeasurementResponseDTO> result = measurementService.getMeasurementsByDevice(device.getId(), pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(dto.getId(), result.getContent().get(0).getId());
        verify(measurementRepository, times(1)).findByDeviceId(device.getId(), pageable);
        verify(measurementMapper, times(1)).toResponseDTO(measurement);
    }

    @Test
    void testSaveMeasurement_Success() {
        // Arrange
        MeasurementRequestDTO requestDTO = MeasurementRequestDTO.builder()
                .deviceId("device123")
                .value(30.5)
                .timestamp(LocalDateTime.now())
                .build();

        Device device = Device.builder().id("device123").name("Device 1").build();
        Measurement measurement = Measurement.builder().value(30.5).timestamp(LocalDateTime.now()).build();
        Measurement savedMeasurement = Measurement.builder().id("1").value(30.5).timestamp(LocalDateTime.now()).build();
        MeasurementResponseDTO responseDTO = MeasurementResponseDTO.builder().id("1").value(30.5).build();

        when(deviceRepository.findById("device123")).thenReturn(Optional.of(device));
        when(measurementMapper.toEntity(requestDTO)).thenReturn(measurement);
        when(measurementRepository.save(measurement)).thenReturn(savedMeasurement);
        when(measurementMapper.toResponseDTO(savedMeasurement)).thenReturn(responseDTO);

        // Act
        MeasurementResponseDTO result = measurementService.save(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(deviceRepository, times(1)).findById("device123");
        verify(measurementMapper, times(1)).toEntity(requestDTO);
        verify(measurementRepository, times(1)).save(measurement);
        verify(alertService, times(1)).createAlertFromMeasurement("device123", 30.5, null);
        verify(measurementMapper, times(1)).toResponseDTO(savedMeasurement);
    }

    @Test
    void testSaveMeasurement_DeviceNotFound() {
        // Arrange
        MeasurementRequestDTO requestDTO = MeasurementRequestDTO.builder().deviceId("invalidDevice").value(12.0).build();
        when(deviceRepository.findById("invalidDevice")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            measurementService.save(requestDTO);
        });
        assertEquals("Device introuvable : invalidDevice", exception.getMessage());
        verify(deviceRepository, times(1)).findById("invalidDevice");
        verifyNoInteractions(measurementMapper, measurementRepository, alertService);
    }

    @Test
    void testExportMeasurements() {
        // Arrange
        Device device =  Device.builder().id("device123").build();

        Measurement measurement = Measurement.builder()
                .id("1")
                .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
                .value(25.5)
                .deviceId(device)
                .build();
        when(measurementRepository.findAll()).thenReturn(List.of(measurement));

        // Act
        byte[] csvData = measurementService.exportMeasurements();

        // Assert
        assertNotNull(csvData);
        String csvString = new String(csvData);
        assertTrue(csvString.contains("id,timestamp,value"));
        assertTrue(csvString.contains("1,2023-01-01T12:00,25.5"));
        verify(measurementRepository, times(1)).findAll();
    }
}

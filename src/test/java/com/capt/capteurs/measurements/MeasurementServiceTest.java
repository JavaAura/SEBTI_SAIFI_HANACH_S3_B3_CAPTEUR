package com.capt.capteurs.measurements;

import com.capt.capteurs.dto.request.MeasurementRequestDTO;
import com.capt.capteurs.dto.response.MeasurementResponseDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.service.impl.AlertServiceImpl;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementServiceImplTest {

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @InjectMocks
    private AlertServiceImpl alertService;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private MeasurementMapper measurementMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetAllMeasurements_NoMeasurements() {
//        // Arrange
//        when(measurementRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // Act
//        Pageable pageable = PageRequest.of(0,3);
//        Page<MeasurementResponseDTO> result = measurementService.getAllMeasurements(pageable);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty(), "Result list should be empty");
//        verify(measurementRepository, times(1)).findAll();
//        verifyNoInteractions(measurementMapper);
//    }
//
//    @Test
//    void testSaveMeasurement_Success() {
//        // Arrange
//        Device device = new Device("123", "DeviceName", null, null, new Date(), null);
//        MeasurementRequestDTO requestDTO = new MeasurementRequestDTO();
//        requestDTO.setDeviceId("123");
//        requestDTO.setTimestamp(LocalDateTime.now());
//        requestDTO.setDeviceName("DeviceName");
//        requestDTO.setValue(25.5);
//        Measurement measurement = new Measurement();
//        Measurement savedMeasurement = new Measurement("1", LocalDateTime.now(), 25.5, device);
//        MeasurementResponseDTO responseDTO = new MeasurementResponseDTO();
//        requestDTO.setDeviceId("1");
//        requestDTO.setTimestamp(LocalDateTime.now());
//        requestDTO.setDeviceName("DeviceName");
//        requestDTO.setValue(25.5);
//        when(deviceRepository.findById("123")).thenReturn(Optional.of(device));
//        when(measurementMapper.toEntity(requestDTO)).thenReturn(measurement);
//        when(measurementRepository.save(measurement)).thenReturn(savedMeasurement);
//        when(measurementMapper.toResponseDTO(savedMeasurement)).thenReturn(responseDTO);
//
//        // Mock behavior for alertService
//        doNothing().when(alertService).createAlertFromMeasurement("123", 25.5, "DeviceName");
//
//        // Act
//        MeasurementResponseDTO result = measurementService.save(requestDTO);
//
//        // Assert
//        assertNotNull(result);
////        assertEquals("123", result.getDeviceId());
//        verify(deviceRepository, times(1)).findById("123");
//        verify(measurementRepository, times(1)).save(measurement);
//        verify(alertService, times(1)).createAlertFromMeasurement("123", 25.5, "DeviceName");
//    }

    @Test
    void testSaveMeasurement_DeviceNotFound() {
        // Arrange
        MeasurementRequestDTO requestDTO = new MeasurementRequestDTO();
        requestDTO.setValue(30.0);
        requestDTO.setDeviceId("invalid_device");

        when(deviceRepository.findById("invalid_device")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            measurementService.save(requestDTO);
        });
        assertEquals("Device introuvable : invalid_device", exception.getMessage());
        verify(deviceRepository, times(1)).findById("invalid_device");
        verifyNoInteractions(measurementMapper, measurementRepository);
    }
}

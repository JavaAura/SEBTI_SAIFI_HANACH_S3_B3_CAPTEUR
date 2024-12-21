package com.capt.capteurs.measurements;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.AlertService;
import com.capt.capteurs.service.impl.MeasurementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementServiceTest {

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

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
        Page<Measurement> emptyPage = Page.empty(pageable);
        when(measurementRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<MeasurementDTO> result = measurementService.getAllMeasurements(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(measurementRepository, times(1)).findAll(pageable);
        verifyNoInteractions(measurementMapper);
    }

    @Test
    void testGetAllMeasurements_WithMeasurements() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Measurement measurement = Measurement.builder().id("1").value(25.0).build();
        MeasurementDTO measurementDTO = MeasurementDTO.builder().id("1").value(25.0).build();

        when(measurementRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(measurement)));
        when(measurementMapper.toResponseDTO(measurement)).thenReturn(measurementDTO);

        // Act
        Page<MeasurementDTO> result = measurementService.getAllMeasurements(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(25.0, result.getContent().get(0).getValue());
        verify(measurementRepository, times(1)).findAll(pageable);
        verify(measurementMapper, times(1)).toResponseDTO(measurement);
    }

    @Test
    void testGetMeasurementsByDevice_NoMeasurements() {
        // Arrange
        String deviceId = "device1";
        Pageable pageable = PageRequest.of(0, 10);
        when(measurementRepository.findByDeviceId(deviceId, pageable)).thenReturn(Collections.emptyList());

        // Act
        Page<MeasurementDTO> result = measurementService.getMeasurementsByDevice(deviceId, pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(measurementRepository, times(1)).findByDeviceId(deviceId, pageable);
        verifyNoInteractions(measurementMapper);
    }

    @Test
    void testSaveMeasurement_Success() {
        // Arrange
        MeasurementDTO measurementDTO = MeasurementDTO.builder().value(25.0).deviceId("device1").build();
        Measurement measurement = Measurement.builder().value(25.0).deviceId("device1").build();
        Measurement savedMeasurement = Measurement.builder().id("1L").value(25.0).deviceId("device1").build();
        MeasurementDTO savedDTO = MeasurementDTO.builder().id("1L").value(25.0).deviceId("device1").build();

        when(measurementMapper.toEntity(measurementDTO)).thenReturn(measurement);
        when(measurementRepository.save(measurement)).thenReturn(savedMeasurement);
        when(measurementMapper.toResponseDTO(savedMeasurement)).thenReturn(savedDTO);

        // Act
        MeasurementDTO result = measurementService.save(measurementDTO);

        // Assert
        assertNotNull(result);
        assertEquals(25.0, result.getValue());
        verify(measurementMapper, times(1)).toEntity(measurementDTO);
        verify(measurementRepository, times(1)).save(measurement);
        verify(measurementMapper, times(1)).toResponseDTO(savedMeasurement);
        verify(alertService, times(1)).createAlertFromMeasurement("device1", 25.0, null);
    }

    @Test
    void testExportMeasurements() {
        // Arrange
        Measurement measurement = Measurement.builder().id("1L").timestamp(LocalDateTime.now()).value(25.0).deviceId("device1").build();
        when(measurementRepository.findAll()).thenReturn(List.of(measurement));

        // Act
        byte[] csvBytes = measurementService.exportMeasurements();

        // Assert
        assertNotNull(csvBytes);
        String csvContent = new String(csvBytes);
        assertTrue(csvContent.contains("id,timestamp,value,deviceId"));
        assertTrue(csvContent.contains("device1"));
        verify(measurementRepository, times(1)).findAll();
    }
}

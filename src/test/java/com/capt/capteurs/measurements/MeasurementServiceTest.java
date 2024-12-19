package com.capt.capteurs.measurements;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.mapper.MeasurementMapper;
import com.capt.capteurs.model.Measurement;
import com.capt.capteurs.repository.MeasurementRepository;
import com.capt.capteurs.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MeasurementServiceTest {

    @Autowired
    private MeasurementService measurementService;

    @MockBean
    private MeasurementRepository measurementRepository;

    @MockBean
    private MeasurementMapper measurementMapper;

    @Test
    public void testCreateMeasurement() {
        MeasurementDTO measurementDto = new MeasurementDTO();
        measurementDto.setValue(25.0);
        measurementDto.setDeviceId("device123");

        Measurement measurement = new Measurement("1", LocalDateTime.now(), 25.0, "device123");
        MeasurementDTO dtoResult = new MeasurementDTO();
        dtoResult.setId("1");
        dtoResult.setValue(25.0);
        dtoResult.setDeviceId("device123");

        when(measurementMapper.toEntity(measurementDto)).thenReturn(measurement);
        when(measurementRepository.save(measurement)).thenReturn(measurement);
        when(measurementMapper.toResponseDTO(measurement)).thenReturn(dtoResult);

        MeasurementDTO result = measurementService.save(measurementDto);

        assertNotNull(result);
        assertEquals(25.0, result.getValue());
    }
}

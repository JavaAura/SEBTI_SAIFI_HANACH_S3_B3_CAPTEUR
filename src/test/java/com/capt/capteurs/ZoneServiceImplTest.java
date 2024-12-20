package com.capt.capteurs;

import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.mapper.ZoneMapper;
import com.capt.capteurs.model.Zone;
import com.capt.capteurs.repository.ZoneRepository;
import com.capt.capteurs.service.impl.ZoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZoneServiceImplTest {

    @InjectMocks
    private ZoneServiceImpl zoneService;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneMapper zoneMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllZones_NoZones() {
        // Arrange
        when(zoneRepository.findAllZonesWithDevices()).thenReturn(Collections.emptyList());

        // Act
        List<ZoneResponseDTO> result = zoneService.getAllZones();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result list should be empty");
        verify(zoneRepository, times(1)).findAllZonesWithDevices();
        verifyNoInteractions(zoneMapper);
    }

    @Test
    void testGetAllZones_WithZones() {
        // Arrange
        Zone zone1 = Zone.builder().id("1").name("Zone 1").build();
        Zone zone2 = Zone.builder().id("2").name("Zone 2").build();

        ZoneResponseDTO dto1 = ZoneResponseDTO.builder().id("1").name("Zone 1").build();
        ZoneResponseDTO dto2 = ZoneResponseDTO.builder().id("2").name("Zone 2").build();

        when(zoneRepository.findAllZonesWithDevices()).thenReturn(List.of(zone1, zone2));
        when(zoneMapper.toResponseDTO(zone1)).thenReturn(dto1);
        when(zoneMapper.toResponseDTO(zone2)).thenReturn(dto2);

        // Act
        List<ZoneResponseDTO> result = zoneService.getAllZones();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Zone 1", result.get(0).getName());
        assertEquals("Zone 2", result.get(1).getName());
        verify(zoneRepository, times(1)).findAllZonesWithDevices();
        verify(zoneMapper, times(2)).toResponseDTO(any(Zone.class));
    }

    @Test
    void testGetZoneById_Success() {
        // Arrange
        String zoneId = "1";
        Zone zone = Zone.builder().id(zoneId).build();
        ZoneResponseDTO dto = ZoneResponseDTO.builder().id(zoneId).build();

        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));
        when(zoneMapper.toResponseDTO(zone)).thenReturn(dto);

        // Act
        ZoneResponseDTO result = zoneService.getZoneById(zoneId);

        // Assert
        assertNotNull(result);
        assertEquals(zoneId, result.getId());
        verify(zoneRepository, times(1)).findById(zoneId);
        verify(zoneMapper, times(1)).toResponseDTO(zone);
    }

    @Test
    void testGetZoneById_NotFound() {
        // Arrange
        String zoneId = "999";
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            zoneService.getZoneById(zoneId);
        });
        assertEquals("Zone not found", exception.getMessage());
        verify(zoneRepository, times(1)).findById(zoneId);
        verifyNoInteractions(zoneMapper);
    }

    @Test
    void testAddZone_Success() {
        // Arrange
        ZoneRequestDTO requestDTO = new ZoneRequestDTO();
        requestDTO.setName("New Zone");
        requestDTO.setType("Type B");
        requestDTO.setLocation("Location Y");

        Zone zone = Zone.builder().id("123").name("New Zone").build();
        ZoneResponseDTO responseDTO = ZoneResponseDTO.builder().id("123").name("New Zone").build();

        when(zoneMapper.toEntity(requestDTO)).thenReturn(zone);
        when(zoneRepository.save(zone)).thenReturn(zone);
        when(zoneMapper.toResponseDTO(zone)).thenReturn(responseDTO);

        // Act
        ZoneResponseDTO result = zoneService.addZone(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Zone", result.getName());
        verify(zoneMapper, times(1)).toEntity(requestDTO);
        verify(zoneRepository, times(1)).save(zone);
        verify(zoneMapper, times(1)).toResponseDTO(zone);
    }
    @Test
    void testAddZone_NullName() {
        // Arrange
        ZoneRequestDTO requestDTO = new ZoneRequestDTO();
        requestDTO.setType("Type C");
        requestDTO.setLocation("Location Z");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            zoneService.addZone(requestDTO);
        });
        assertEquals("Zone name is required", exception.getMessage());
        verifyNoInteractions(zoneRepository, zoneMapper);
    }

}

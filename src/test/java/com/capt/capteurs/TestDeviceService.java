package com.capt.capteurs;
import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.mapper.Devicemapper;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.Zone;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.repository.ZoneRepository;
import com.capt.capteurs.service.impl.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDeviceService {

    @InjectMocks
    private DeviceServiceImpl deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private Devicemapper devicemapper;

    private DeviceRequestDTO deviceRequestDTO;
    private Device device;
    private DeviceResponseDTO deviceResponseDTO;
    private Zone zone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock data
        deviceRequestDTO = new DeviceRequestDTO();
        deviceRequestDTO.setZoneId("zone1");

        zone = new Zone();
        zone.setId("zone1");

        device = new Device();
        device.setId("67653d3910f87e3d99d180ef");
        device.setZone(zone);

        deviceResponseDTO = new DeviceResponseDTO();
        deviceResponseDTO.setId("67653d3910f87e3d99d180ef");
        deviceResponseDTO.setName("zone1");
    }

    @Test
    void testGetAllDevices() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Device> devicePage = new PageImpl<>(Arrays.asList(device));

        when(deviceRepository.findAll(pageable)).thenReturn(devicePage);
        when(devicemapper.toDeviceResponseDTO(device)).thenReturn(deviceResponseDTO);

        // Test scenario 1: Successful retrieval of all devices
        var result = deviceService.getAllDevices(0, 5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(deviceResponseDTO, result.get(0));

        // Test scenario 2: Empty list
        when(deviceRepository.findAll(pageable)).thenReturn(Page.empty());
        result = deviceService.getAllDevices(0, 5);
        assertTrue(result.isEmpty());

        // Test scenario 3: Exception during repository access (e.g., database error)
        when(deviceRepository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> deviceService.getAllDevices(0, 5));
    }

    @Test
    void testGetDevicesByZone() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Device> devicePage = new PageImpl<>(Arrays.asList(device));

        when(deviceRepository.findByZoneId("zone1", pageable)).thenReturn(devicePage);
        when(devicemapper.toDeviceResponseDTO(device)).thenReturn(deviceResponseDTO);

        // Test scenario 1: Successful retrieval of devices by zone
        var result = deviceService.getDevicesByZone("zone1", 0, 5);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(deviceResponseDTO, result.get(0));

        // Test scenario 2: No devices in zone
        when(deviceRepository.findByZoneId("zone1", pageable)).thenReturn(Page.empty());
        result = deviceService.getDevicesByZone("zone1", 0, 5);
        assertTrue(result.isEmpty());

        // Test scenario 3: Exception during repository access (e.g., database error)
        when(deviceRepository.findByZoneId("zone1", pageable)).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> deviceService.getDevicesByZone("zone1", 0, 5));
    }

    @Test
    void testAddDevice() {
        when(devicemapper.toDevice(deviceRequestDTO)).thenReturn(device);
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zone));
        when(deviceRepository.save(device)).thenReturn(device);
        when(devicemapper.toDeviceResponseDTO(device)).thenReturn(deviceResponseDTO);

        // Test scenario 1: Successfully add a device
        var result = deviceService.addDevice(deviceRequestDTO);
        assertNotNull(result);
        assertEquals(deviceResponseDTO, result);

        // Test scenario 2: Zone not found (empty Optional)
        when(zoneRepository.findById("zone1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> deviceService.addDevice(deviceRequestDTO));

        // Test scenario 3: Exception during repository save
        when(deviceRepository.save(device)).thenThrow(new RuntimeException("Save error"));
        assertThrows(RuntimeException.class, () -> deviceService.addDevice(deviceRequestDTO));
    }
}

package com.capt.capteurs;

import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.model.DeviceStatus;
import com.capt.capteurs.model.DeviceType;
import com.capt.capteurs.model.Zone;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.repository.ZoneRepository;
import com.capt.capteurs.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DeviceServiceIntegrationTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Zone zone;
    private Device device;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Device.class);
        mongoTemplate.dropCollection(Zone.class);

        zone = new Zone();
        zone.setId("zone1");
        zone.setName("Zone Test");
        zoneRepository.save(zone);

        device = new Device();
        device.setId("device1");
        device.setName("Capteur Test");
        device.setDeviceType(DeviceType.HUMIDITY);
        device.setStatus(DeviceStatus.ACTIVE);
        device.setLastCommunication(new Date());
        device.setZone(zone);
        deviceRepository.save(device);
    }

    @Test
    void testGetAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllDevices(0, 10);

        assertNotNull(devices);
        assertEquals(1, devices.size());
        assertEquals("Capteur Test", devices.get(0).getName());
        assertEquals("Zone Test", devices.get(0).getName_zone());
    }

    @Test
    void testGetDevicesByZone() {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByZone("zone1", 0, 10);

        assertNotNull(devices);
        assertEquals(1, devices.size());
        assertEquals("Capteur Test", devices.get(0).getName());
    }

    @Test
    void testAddDevice() {

        DeviceRequestDTO newDeviceRequest = new DeviceRequestDTO();
        newDeviceRequest.setName("Nouveau Capteur");
        newDeviceRequest.setDeviceType(DeviceType.HUMIDITY);
        newDeviceRequest.setStatus(DeviceStatus.INACTIVE);
        newDeviceRequest.setLastCommunication(new Date());
        newDeviceRequest.setZoneId("zone1");

        DeviceResponseDTO savedDevice = deviceService.addDevice(newDeviceRequest);

        assertNotNull(savedDevice);
        assertEquals("Nouveau Capteur", savedDevice.getName());
        assertEquals("Zone Test", savedDevice.getName_zone());

        Device deviceInDb = deviceRepository.findById(savedDevice.getId()).orElse(null);
        assertNotNull(deviceInDb);
        assertEquals("Nouveau Capteur", deviceInDb.getName());
    }
}

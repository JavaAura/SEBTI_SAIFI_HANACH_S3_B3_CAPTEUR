package com.capt.capteurs;

import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.model.Zone;
import com.capt.capteurs.repository.ZoneRepository;
import com.capt.capteurs.service.ZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ZoneServiceIntegrationTest {

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Zone zone;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Zone.class);

        zone = Zone.builder()
                .id("zone1")
                .name("Zone Test")
                .type("Residential")
                .location("Location Test")
                .build();

        zoneRepository.save(zone);
    }

    @Test
    void testGetAllZones() {
        List<ZoneResponseDTO> zones = zoneService.getAllZones();

        assertNotNull(zones);
        assertEquals(1, zones.size());
        assertEquals("Zone Test", zones.get(0).getName());
        assertEquals("Residential", zones.get(0).getType());
        assertEquals("Location Test", zones.get(0).getLocation());
    }

    @Test
    void testGetZoneById() {
        ZoneResponseDTO retrievedZone = zoneService.getZoneById("zone1");

        assertNotNull(retrievedZone);
        assertEquals("Zone Test", retrievedZone.getName());
        assertEquals("Residential", retrievedZone.getType());
        assertEquals("Location Test", retrievedZone.getLocation());
    }

    @Test
    void testAddZone() {
        ZoneRequestDTO newZoneRequest = new ZoneRequestDTO();
        newZoneRequest.setName("New Zone");
        newZoneRequest.setType("Commercial");
        newZoneRequest.setLocation("New Location");

        ZoneResponseDTO savedZone = zoneService.addZone(newZoneRequest);

        assertNotNull(savedZone);
        assertEquals("New Zone", savedZone.getName());
        assertEquals("Commercial", savedZone.getType());
        assertEquals("New Location", savedZone.getLocation());

        Zone zoneInDb = zoneRepository.findById(savedZone.getId()).orElse(null);
        assertNotNull(zoneInDb);
        assertEquals("New Zone", zoneInDb.getName());
    }

    @Test
    void testGetZoneById_ZoneNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            zoneService.getZoneById("nonexistent");
        });

        assertEquals("Zone not found", exception.getMessage());
    }

    @Test
    void testAddZone_InvalidInput() {
        ZoneRequestDTO invalidZoneRequest = new ZoneRequestDTO();
        invalidZoneRequest.setName("");
        invalidZoneRequest.setType("Commercial");
        invalidZoneRequest.setLocation("New Location");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            zoneService.addZone(invalidZoneRequest);
        });

        assertEquals("Zone name is required", exception.getMessage());
    }
}

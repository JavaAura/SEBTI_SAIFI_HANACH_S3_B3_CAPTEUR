package com.capt.capteurs.controller;

import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.service.ZoneService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping({"/user/zones", "/admin/zones"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        log.info("Fetching all zones");
        List<ZoneResponseDTO> zones = zoneService.getAllZones();
        log.info("Successfully fetched {} zones", zones.size());
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/user/zones/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ZoneResponseDTO> getZoneById(@PathVariable String id) {
        log.info("Fetching zone with ID: {}", id);
        ZoneResponseDTO zone = zoneService.getZoneById(id);
        log.info("Successfully fetched zone: {}", zone);
        return ResponseEntity.ok(zone);
    }

    @PostMapping("/admin/zones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneResponseDTO> addZone(@Valid @RequestBody ZoneRequestDTO zoneRequestDTO) {
        log.info("Adding a new zone: {}", zoneRequestDTO);
        ZoneResponseDTO zoneResponse = zoneService.addZone(zoneRequestDTO);
        log.info("Zone added successfully with ID: {}", zoneResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneResponse);
    }
}

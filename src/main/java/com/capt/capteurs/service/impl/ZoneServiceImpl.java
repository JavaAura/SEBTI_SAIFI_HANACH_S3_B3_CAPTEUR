package com.capt.capteurs.service.impl;


import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;
import com.capt.capteurs.mapper.ZoneMapper;
import com.capt.capteurs.model.Zone;
import com.capt.capteurs.repository.ZoneRepository;
import com.capt.capteurs.service.ZoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ZoneServiceImpl implements ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneMapper zoneMapper;

    @Override
    public List<ZoneResponseDTO> getAllZones() {
        log.debug("Retrieving all zones from the database");
        List<Zone> zones = zoneRepository.findAll();
        log.debug("Retrieved {} zones", zones.size());
        return zones.stream().map(zoneMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ZoneResponseDTO getZoneById(String id) {
        log.debug("Retrieving zone with ID: {}", id);
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Zone with ID {} not found", id);
                    throw new IllegalArgumentException("Zone not found");
                });
        log.debug("Retrieved zone: {}", zone);
        return zoneMapper.toResponseDTO(zone);
    }

    @Override
    public ZoneResponseDTO addZone(ZoneRequestDTO zoneRequestDTO) {
        log.debug("Mapping ZoneRequestDTO to Zone entity");
        Zone zone = zoneMapper.toEntity(zoneRequestDTO);
        log.debug("Saving zone to the database");
        zone = zoneRepository.save(zone);
        log.info("Zone with ID {} saved successfully", zone.getId());
        return zoneMapper.toResponseDTO(zone);
    }
}

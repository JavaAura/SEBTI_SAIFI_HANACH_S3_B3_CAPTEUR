package com.capt.capteurs.service;


import com.capt.capteurs.dto.request.ZoneRequestDTO;
import com.capt.capteurs.dto.response.ZoneResponseDTO;

import java.util.List;

public interface ZoneService {
    ZoneResponseDTO addZone(ZoneRequestDTO zoneRequestDTO);
    ZoneResponseDTO getZoneById(String id);
    List<ZoneResponseDTO> getAllZones();
}

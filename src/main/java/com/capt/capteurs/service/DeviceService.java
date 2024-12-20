package com.capt.capteurs.service;

import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;

import java.util.List;

public interface DeviceService {

    List<DeviceResponseDTO> getAllDevices(int page, int size);

    List<DeviceResponseDTO> getDevicesByZone(String zoneId, int page, int size);

    DeviceResponseDTO addDevice(DeviceRequestDTO deviceRequestDTO);
}

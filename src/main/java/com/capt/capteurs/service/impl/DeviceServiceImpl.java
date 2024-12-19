package com.capt.capteurs.service.impl;

import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.mapper.Devicemapper;
import com.capt.capteurs.model.Device;
import com.capt.capteurs.repository.DeviceRepository;
import com.capt.capteurs.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private Devicemapper devicemapper;

    @Override
    public List<DeviceResponseDTO> getAllDevices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = deviceRepository.findAll(pageable);
        return devicePage.getContent().stream()
                .map(devicemapper::toDeviceResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceResponseDTO> getDevicesByZone(String zoneId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = deviceRepository.findByZoneId(zoneId, pageable);
        return devicePage.getContent().stream()
                .map(devicemapper::toDeviceResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceResponseDTO addDevice(DeviceRequestDTO deviceRequestDTO) {
        Device device = devicemapper.toDevice(deviceRequestDTO);
        deviceRepository.save(device);
        return devicemapper.toDeviceResponseDTO(device);
    }
}

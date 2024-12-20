package com.capt.capteurs.controller;
import com.capt.capteurs.dto.request.DeviceRequestDTO;
import com.capt.capteurs.dto.response.DeviceResponseDTO;
import com.capt.capteurs.service.DeviceService;
import com.capt.capteurs.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceController {

    @Autowired
    private  DeviceService deviceService;

    @Autowired
    private  ZoneService zoneService;



    @GetMapping("/user/devices")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<DeviceResponseDTO> getAllDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return deviceService.getAllDevices(page, size);
    }


    @GetMapping("/user/devices/zone/{zoneId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<DeviceResponseDTO> getDevicesByZone(@PathVariable String zoneId,
                                                    @RequestParam int page,
                                                    @RequestParam int size) {
        return deviceService.getDevicesByZone(zoneId, page, size);
    }

    @PostMapping("/admin/devices")
    @PreAuthorize("hasRole('ADMIN')")
    public DeviceResponseDTO addDevice(@RequestBody DeviceRequestDTO deviceRequestDTO) {
        return deviceService.addDevice(deviceRequestDTO);
    }

}

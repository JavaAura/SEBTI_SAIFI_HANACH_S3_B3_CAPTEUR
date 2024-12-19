package com.capt.capteurs.dto.response;

import com.capt.capteurs.model.DeviceStatus;
import com.capt.capteurs.model.DeviceType;

import java.time.LocalDateTime;

public class DeviceResponseDTO {
    private String id;
    private String name;
    private DeviceType deviceType;
    private DeviceStatus status;
    private LocalDateTime lastCommunication;
    private String zoneId;
}

package com.capt.capteurs.dto.request;

import com.capt.capteurs.model.DeviceStatus;
import com.capt.capteurs.model.DeviceType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class DeviceRequestDTO {
    private String name;
    private DeviceType deviceType;
    private DeviceStatus status;
    private LocalDateTime lastCommunication;
    private String zoneId;


}
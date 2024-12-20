package com.capt.capteurs.dto.request;

import com.capt.capteurs.model.DeviceStatus;
import com.capt.capteurs.model.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestDTO {
    private String name;
    private DeviceType deviceType;
    private DeviceStatus status;
    private Date lastCommunication;
    private String zoneId;




}
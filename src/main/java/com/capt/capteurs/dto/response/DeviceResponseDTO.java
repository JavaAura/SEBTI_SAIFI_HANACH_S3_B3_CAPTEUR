package com.capt.capteurs.dto.response;

import com.capt.capteurs.model.DeviceStatus;
import com.capt.capteurs.model.DeviceType;
import com.capt.capteurs.model.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {
    private String id;
    private String name;
    private DeviceType deviceType;
    private DeviceStatus status;
    private Date lastCommunication;
    private String name_zone;
}

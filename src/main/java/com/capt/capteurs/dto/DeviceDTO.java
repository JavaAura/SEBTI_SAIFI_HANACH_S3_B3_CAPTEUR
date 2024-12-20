package com.capt.capteurs.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDTO {

    @NotNull(message = "Device ID cannot be null")
    private String id;

    @NotNull(message = "Device name cannot be null")
    private String name;

}

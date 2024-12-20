package com.capt.capteurs.dto.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class MeasurementRequestDTO {
    private LocalDateTime timestamp;
    private double value;
    private String deviceId;
    private String deviceName;
}

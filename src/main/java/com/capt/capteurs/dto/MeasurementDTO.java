package com.capt.capteurs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MeasurementDTO {
    private String id;
    private LocalDateTime timestamp;
    private double value;
    private String deviceId;
    private String deviceType;
}

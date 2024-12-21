package com.capt.capteurs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MeasurementResponseDTO {
    private String id;
    private LocalDateTime timestamp;
    private double value;
    private String deviceId;
    private String deviceName;
}

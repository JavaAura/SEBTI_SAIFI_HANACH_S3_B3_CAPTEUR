package com.capt.capteurs.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlertResponseDTO {
    private String id;
    private String severity;
    private String message;
    private LocalDateTime timestamp;
    private String deviceId;
    private String deviceName;
}
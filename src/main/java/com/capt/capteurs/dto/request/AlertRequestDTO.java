package com.capt.capteurs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class AlertRequestDTO {
    @NotBlank(message = "Severity name is required")
    private String severity;
    @NotBlank(message = "Message name is required")
    private String message;
    private LocalDateTime timestamp;
    private String deviceId;
}
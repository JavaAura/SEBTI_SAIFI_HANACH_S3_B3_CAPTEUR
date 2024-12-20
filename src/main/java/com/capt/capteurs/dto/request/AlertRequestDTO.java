package com.capt.capteurs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
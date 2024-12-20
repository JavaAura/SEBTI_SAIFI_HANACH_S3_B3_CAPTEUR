package com.capt.capteurs.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "alerts")
public class Alert {
    @Id
    private String id;
    private String severity;
    private String message;
    private LocalDateTime timestamp;
    private String deviceId;
}
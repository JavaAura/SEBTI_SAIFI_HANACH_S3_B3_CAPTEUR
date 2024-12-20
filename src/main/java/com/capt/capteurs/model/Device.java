package com.capt.capteurs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "devices")
public class Device {

    @Id
    private String id;
    private String name;
    private DeviceType deviceType;
    private DeviceStatus status;
    private Date lastCommunication;

    @DBRef
    private Zone zone;


}

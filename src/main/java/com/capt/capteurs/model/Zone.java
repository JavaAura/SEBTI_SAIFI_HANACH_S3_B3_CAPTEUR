package com.capt.capteurs.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "zones")
public class Zone {

    @Id
    private String id;

    private String name;

    private String type;

    private String location;

    @DBRef
    private List<Device> devices;
}

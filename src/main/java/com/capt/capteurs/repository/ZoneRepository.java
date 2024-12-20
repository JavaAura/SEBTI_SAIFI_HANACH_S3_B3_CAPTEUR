package com.capt.capteurs.repository;

import com.capt.capteurs.model.Zone;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ZoneRepository extends MongoRepository<Zone, String> {
    boolean existsByName(String name);

    @Aggregation(pipeline = {
            "{ $lookup: { from: 'devices', localField: '_id', foreignField: 'zone.$id', as: 'devices' } }"
    })
    List<Zone> findAllZonesWithDevices();

}

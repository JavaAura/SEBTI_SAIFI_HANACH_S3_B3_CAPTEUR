package com.capt.capteurs.repository;

import com.capt.capteurs.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZoneRepository extends MongoRepository<Zone, String> {
    boolean existsByName(String name);
}

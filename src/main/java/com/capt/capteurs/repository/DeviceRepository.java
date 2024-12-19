package com.capt.capteurs.repository;

import com.capt.capteurs.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    Page<Device> findByZoneId(String zoneId, Pageable pageable);

}

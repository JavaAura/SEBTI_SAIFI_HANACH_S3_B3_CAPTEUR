package com.capt.capteurs.repository;

import com.capt.capteurs.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByDeviceId(String deviceId);
    List<Alert> findBySeverity(String severity);
}
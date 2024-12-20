package com.capt.capteurs.controller;

import com.capt.capteurs.dto.MeasurementDTO;
import com.capt.capteurs.dto.response.AlertResponseDTO;
import com.capt.capteurs.service.AlertService;
import com.capt.capteurs.service.MeasurementService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }


    @GetMapping("/api/admin/alerts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlertResponseDTO>> getAllAlertsForAdmins() {
        List<AlertResponseDTO> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/api/user/alerts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AlertResponseDTO>> getAllAlertsForUsers() {
        List<AlertResponseDTO> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

}
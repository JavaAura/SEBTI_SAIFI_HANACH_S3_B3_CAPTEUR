package com.capt.capteurs.controller;

import com.capt.capteurs.dto.RoleDTO;
import com.capt.capteurs.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO createdRole = roleService.addRole(roleDTO);
            return ResponseEntity.ok(createdRole);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

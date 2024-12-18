package com.capt.capteurs.service.impl;

import com.capt.capteurs.dto.RoleDTO;
import com.capt.capteurs.mapper.RoleMapper;
import com.capt.capteurs.model.Role;
import com.capt.capteurs.repository.RoleRepository;
import com.capt.capteurs.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);

        if (roleRepository.findByName(role.getName()) != null) {
            throw new IllegalArgumentException("Role with name '" + role.getName() + "' already exists.");
        }

        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }
}

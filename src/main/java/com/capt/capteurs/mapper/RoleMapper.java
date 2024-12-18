package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.RoleDTO;
import com.capt.capteurs.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);
}
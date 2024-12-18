package com.capt.capteurs.mapper;

import com.capt.capteurs.dto.UserDTO;
import com.capt.capteurs.model.Role;
import com.capt.capteurs.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRolesToStrings(user.getRoles()))")
    UserDTO toDTO(User user);

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserDTO userDTO);

    default List<String> mapRolesToStrings(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }
}
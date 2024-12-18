package com.capt.capteurs.service;

import com.capt.capteurs.dto.UserDTO;
import com.capt.capteurs.model.User;

import java.util.List;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    void updateUserRoles(String id, List<String> roles);
    User loadUserByUsername(String username);
}

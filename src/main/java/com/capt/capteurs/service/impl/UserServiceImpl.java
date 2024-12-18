package com.capt.capteurs.service.impl;


import com.capt.capteurs.dto.UserDTO;
import com.capt.capteurs.exception.ResourceNotFoundException;
import com.capt.capteurs.mapper.UserMapper;
import com.capt.capteurs.model.Role;
import com.capt.capteurs.model.User;
import com.capt.capteurs.repository.RoleRepository;
import com.capt.capteurs.repository.UserRepository;
import com.capt.capteurs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName))
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            roles.add(roleRepository.findByName("USER"));
        }

        user.setRoles(roles);

        return userMapper.toDTO(userRepository.save(user));
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void updateUserRoles(String id, List<String> roles) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> updatedRoles = roles.stream().map(roleRepository::findByName).collect(Collectors.toList());
        user.setRoles(updatedRoles);
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

}

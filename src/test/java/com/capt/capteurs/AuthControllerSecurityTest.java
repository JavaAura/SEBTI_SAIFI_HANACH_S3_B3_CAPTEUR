package com.capt.capteurs;

import com.capt.capteurs.controller.AuthController;
import com.capt.capteurs.dto.AuthResponse;
import com.capt.capteurs.dto.LoginRequest;
import com.capt.capteurs.dto.UserDTO;
import com.capt.capteurs.security.JwtTokenProvider;
import com.capt.capteurs.service.CustomUserDetails;
import com.capt.capteurs.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerSecurityTest {

    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationManager, jwtTokenProvider, userService);
    }

    @Test
    void login_ShouldReturnTokenOnValidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("password");

        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        CustomUserDetails userDetails = new CustomUserDetails("user1", "password", true, authorities);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenProvider.generateToken("user1", authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList())))
                .thenReturn("mocked-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mocked-token", response.getBody().getToken());
        assertEquals("user1", response.getBody().getUsername());
        assertEquals(authorities, response.getBody().getRoles());
    }

    @Test
    void login_ShouldThrowExceptionOnInvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        try {
            authController.login(request);
        } catch (RuntimeException e) {
            assertEquals("Invalid credentials", e.getMessage());
        }
    }

    @Test
    void register_ShouldReturnTokenOnSuccessfulRegistration() {
        // Arrange
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setPassword("password");
        userDTO.setRoles(Collections.singletonList("ROLE_USER"));

        UserDTO registeredUser = new UserDTO();
        registeredUser.setUsername("newuser");
        registeredUser.setPassword("password");
        registeredUser.setRoles(Collections.singletonList("ROLE_USER"));

        when(userService.register(userDTO)).thenReturn(registeredUser);
        when(jwtTokenProvider.generateToken("newuser", registeredUser.getRoles()))
                .thenReturn("mocked-token");

        // Act
        ResponseEntity<AuthResponse> response = authController.register(userDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mocked-token", response.getBody().getToken());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals(authorities, response.getBody().getRoles());
    }

    @Test
    void register_ShouldThrowExceptionOnInvalidData() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("");  // Invalid username
        userDTO.setPassword("password");
        userDTO.setRoles(Collections.singletonList("ROLE_USER"));

        when(userService.register(userDTO)).thenThrow(new RuntimeException("Invalid user data"));

        // Act & Assert
        try {
            authController.register(userDTO);
        } catch (RuntimeException e) {
            assertEquals("Invalid user data", e.getMessage());
        }
    }
}

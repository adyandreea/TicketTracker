package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.auth.AuthenticationRequest;
import com.andreea.ticket_tracker.auth.AuthenticationResponse;
import com.andreea.ticket_tracker.auth.AuthenticationService;
import com.andreea.ticket_tracker.auth.RegisterRequest;
import com.andreea.ticket_tracker.dto.request.UserRequestDTO;
import com.andreea.ticket_tracker.exceptions.UserNotFoundException;
import com.andreea.ticket_tracker.mapper.UserDTOMapper;
import com.andreea.ticket_tracker.security.config.JwtProvider;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.andreea.ticket_tracker.entity.Role.MANAGER;
import static com.andreea.ticket_tracker.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDTOMapper userDTOMapper;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstname("firstname")
                .lastname("lastname")
                .username("user_test")
                .email("test@example.com")
                .role(MANAGER)
                .build();
    }

    @Test
    void testRegister(){
        RegisterRequest request = new RegisterRequest("User", "User", "user_test", "test@example.com", "password123", MANAGER);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(jwtProvider.generateToken(any(User.class))).thenReturn("mocked_jwt_token");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getToken());
        verify(repository, times(1)).save(any(User.class));
        verify(jwtProvider).generateToken(any(User.class));
    }

    @Test
    void testAuthenticate(){
        AuthenticationRequest request = new AuthenticationRequest("User","password123");
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("mocked_jwt_token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("mocked_jwt_token",response.getToken());
        verify(authenticationManager).authenticate((any(Authentication.class)));
    }

    @Test
    void testGetAllUsers(){
        when(repository.findAll()).thenReturn(List.of(user));

        var result = authenticationService.getAllUsers();

        assertNotNull(result);
        verify(repository,times(1)).findAll();
    }

    @Test
    void testDeleteUser(){
     when(repository.findById(1L)).thenReturn(Optional.of(user));

     authenticationService.deleteUser(1L);

     verify(repository, times(1)).findById(1L);
     verify(repository).deleteById(1L);
    }

    @Test
    void testUpdateUser(){
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstname("Huang");
        dto.setLastname("Ana");
        dto.setUsername("ana");
        dto.setEmail("ana@gmail.com");
        dto.setRole(USER);

        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        authenticationService.updateUser(1L, dto);

        verify(repository).save(user);
        assertEquals("Huang", user.getFirstname());
        assertEquals("Ana", user.getLastname());
        assertEquals("ana", user.getUsername());
        assertEquals("ana@gmail.com", user.getEmail());
        assertEquals(USER, user.getRole());
    }

    @Test
    void testGetUserWhenNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authenticationService.deleteUser(1L));
        verify(repository, never()).deleteById(anyLong());
    }
}

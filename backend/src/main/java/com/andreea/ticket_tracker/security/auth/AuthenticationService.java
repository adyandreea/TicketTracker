package com.andreea.ticket_tracker.security.auth;

import com.andreea.ticket_tracker.security.config.JwtProvider;
import com.andreea.ticket_tracker.security.user.User;
import com.andreea.ticket_tracker.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        repository.save(user);

        var jwtToken = jwtProvider.generateToken(request.getUsername());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var jwtToken = jwtProvider.generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public void deleteUser(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    public UserResponseDTO updateUser(Integer id, UserRequestDTO request) {
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User updatedUser = repository.save(user);

        return userMapper.toDTO(updatedUser);
    }
}

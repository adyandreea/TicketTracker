package com.andreea.ticket_tracker.security.auth;

import com.andreea.ticket_tracker.security.auth.UserResponseDTO;
import com.andreea.ticket_tracker.security.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDto(User user) {
        if (user == null) return null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}

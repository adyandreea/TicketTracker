package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper utility to convert between User entities and DTOs.
 */
@Component
public class UserDTOMapper {

    /**
     * Converts a User entity to a UserResponseDTO.
     * @param user the entity to convert
     * @return the populated response DTO, or null if the input is null
     */
    public UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}

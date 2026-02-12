package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.response.UserResponseDTO;
import com.andreea.ticket_tracker.entity.Role;
import com.andreea.ticket_tracker.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDTOMapperTest {

    @Test
    void testToDTO(){
        User user = new User();
        user.setId(1);
        user.setFirstname("Huang");
        user.setLastname("Ana");
        user.setUsername("ana");
        user.setEmail("ana@gmail.com");
        user.setRole(Role.MANAGER);

        UserResponseDTO dto = new UserDTOMapper().toDTO(user);

        assertThat(user).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getFirstname()).isEqualTo(user.getFirstname());
        assertThat(dto.getLastname()).isEqualTo(user.getLastname());
        assertThat(dto.getUsername()).isEqualTo(user.getUsername());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getRole()).isEqualTo(user.getRole());
    }
}

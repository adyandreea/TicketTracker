package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.auth.AuthenticationRequest;
import com.andreea.ticket_tracker.auth.RegisterRequest;
import com.andreea.ticket_tracker.dto.request.UserRequestDTO;
import com.andreea.ticket_tracker.entity.Role;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testRegisterUser() throws Exception {

        RegisterRequest request = new RegisterRequest("Huang", "Ana", "ana", "ana@gmail.com", "password123", Role.USER);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testAuthenticateUser() throws Exception{
        User user = new User();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("admin@gmail.com");
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthenticationRequest request = new AuthenticationRequest("admin", "password");

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllUsers() throws Exception{
        User user = new User();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setUsername("admin");
        user.setPassword("password123");
        user.setEmail("admin@gmail.com");
        user.setRole(Role.USER);

        userRepository.save(user);

        mockMvc.perform(get("/api/v1/auth/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstname", is("admin")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteUserById() throws Exception{
        User user = new User();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setUsername("admin");
        user.setPassword("password123");
        user.setEmail("admin@gmail.com");
        user.setRole(Role.USER);

        userRepository.save(user);
        Long id = user.getId();

        mockMvc.perform(delete("/api/v1/auth/users/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testUpdateUsersById() throws Exception{
        User user = new User();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setUsername("admin");
        user.setPassword("password123");
        user.setEmail("admin@gmail.com");
        user.setRole(Role.ADMIN);

        userRepository.save(user);
        Long id = user.getId();

        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstname("user firstname");
        dto.setLastname("user lastname");
        dto.setUsername("username");
        dto.setEmail("user@gmail.com");
        dto.setRole(Role.USER);

        mockMvc.perform(put("/api/v1/auth/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.firstname").value("user firstname"))
                .andExpect(jsonPath("$.lastname").value("user lastname"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testUserNotFound() throws Exception{
        Long invalidId = 999L;

        mockMvc.perform(delete("/api/v1/auth/users/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("user_not_found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"USER"})
    void testGetCurrentUser() throws Exception {
        User user = new User();
        user.setFirstname("User");
        user.setLastname("User");
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("user@test.com");
        user.setRole(Role.USER);
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("username")))
                .andExpect(jsonPath("$.email", is("user@test.com")));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"USER"})
    void testUpdateProfilePicture() throws Exception {
        User user = new User();
        user.setFirstname("User");
        user.setLastname("User");
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("user@test.com");
        user.setRole(Role.USER);
        userRepository.save(user);

        String base64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

        mockMvc.perform(put("/api/v1/auth/users/profile-picture")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(base64Image))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findByUsername("username").orElseThrow();
        assertEquals(base64Image, updatedUser.getProfilePicture());
    }

    @Test
    @WithMockUser(username = "username", authorities = {"USER"})
    void testDeleteProfilePicture() throws Exception {
        User user = new User();
        user.setFirstname("User");
        user.setLastname("User");
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("user@test.com");
        user.setRole(Role.USER);
        user.setProfilePicture("existent-image-data");
        userRepository.save(user);

        mockMvc.perform(delete("/api/v1/auth/users/profile-picture")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findByUsername("username").orElseThrow();
        assertNull(updatedUser.getProfilePicture());
    }
}

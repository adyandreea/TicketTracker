package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.entity.Role;
import com.andreea.ticket_tracker.entity.User;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        projectRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateProject() throws Exception{

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("Test");
        dto.setDescription("Description");

        mockMvc.perform(post("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllProjects() throws Exception{

        Project p1 = new Project();
        p1.setName("Test 1");
        p1.setDescription("Desc 1");

        Project p2 = new Project();
        p2.setName("Test 2");
        p2.setDescription("Desc 2");

        projectRepository.save(p1);
        projectRepository.save(p2);

        mockMvc.perform(get("/api/v1/projects")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Test 1"))
                .andExpect(jsonPath("$[1].name").value("Test 2"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetProjectById() throws Exception {
        Project p = new Project();
        p.setName("Test 1");
        p.setDescription("Desc");

        projectRepository.save(p);

        Long id = p.getId();

        mockMvc.perform(get("/api/v1/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test 1"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testUpdateProject() throws Exception {
        Project p = new Project();
        p.setName("Old");
        p.setDescription("Old Description");

        projectRepository.save(p);
        Long id = p.getId();

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("New project");
        dto.setDescription("New Description");

        mockMvc.perform(put("/api/v1/projects/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.name").value("New project"))
                .andExpect(jsonPath("$.description").value("New Description"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteProject() throws Exception {
        Project p = new Project();
        p.setName("Test");
        p.setDescription("Desc");

        projectRepository.save(p);
        Long id = p.getId();

        mockMvc.perform(delete("/api/v1/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testProjectNotFound() throws Exception {

        Long invalidId = 999L;

        mockMvc.perform(get("/api/v1/projects/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("project_not_found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testValidationOnCreateProject() throws Exception {

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("");
        dto.setDescription("a".repeat(300));

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", org.hamcrest.Matchers.hasSize(3)))

                .andExpect(jsonPath("$.fieldErrors[?(@.field=='name')].message",
                        org.hamcrest.Matchers.hasItems("name_required", "name_length_invalid")))

                .andExpect(jsonPath("$.fieldErrors[?(@.field=='description')].message",
                        org.hamcrest.Matchers.hasItem("description_too_long")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAssignUserToProject() throws Exception{
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Work project");
        project = projectRepository.save(project);

        User user = new User();
        user.setFirstname("User");
        user.setLastname("User");
        user.setUsername("user1");
        user.setPassword("password123");
        user.setEmail("user@gmail.com");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        mockMvc.perform(post("/api/v1/projects/" + project.getId()+"/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User assigned to project successfully"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetProjectMembers() throws Exception{
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Work project");
        project = projectRepository.save(project);

        User u1 = new User();
        u1.setFirstname("User");
        u1.setLastname("User");
        u1.setUsername("user1");
        u1.setPassword("password123");
        u1.setEmail("user@gmail.com");
        u1.setRole(Role.USER);
        u1 = userRepository.save(u1);

        project.addUser(u1);
        projectRepository.save(project);

        mockMvc.perform(get("/api/v1/projects/" + project.getId() + "/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testRemoveUserFromProject() throws Exception{
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Work project");
        project = projectRepository.save(project);

        User u1 = new User();
        u1.setFirstname("User");
        u1.setLastname("User");
        u1.setUsername("user1");
        u1.setPassword("password123");
        u1.setEmail("user@gmail.com");
        u1.setRole(Role.USER);
        u1 = userRepository.save(u1);

        project.addUser(u1);
        projectRepository.save(project);

        project.removeUser(u1);
        projectRepository.save(project);

        mockMvc.perform(delete("/api/v1/projects/"  + project.getId()+"/users/" + u1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User removed from project successfully"));
    }
}

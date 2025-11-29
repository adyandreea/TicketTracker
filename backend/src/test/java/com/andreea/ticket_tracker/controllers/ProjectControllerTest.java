package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.ProjectRequestDTO;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.repository.ProjectRepository;
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project created successfully"))
                .andExpect(jsonPath("$.status").value(201));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project updated successfully"));
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
}

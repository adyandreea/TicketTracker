package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        boardRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testCreateBoard() throws Exception {

        Project project = new Project();
        project.setName("Board");
        project.setDescription("Description");
        project = projectRepository.save(project);

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("Test");
        dto.setDescription("Description");
        dto.setProjectId(project.getId());

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Board created successfully"))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetAllBoards() throws Exception {

        Project project = new Project();
        project.setName("Project 1");
        project.setDescription("Desc 1");
        project = projectRepository.save(project);

        Board board1 = new Board();
        board1.setName("Board 1");
        board1.setDescription("Desc 1");
        board1.setProject(project);

        Board board2 = new Board();
        board2.setName("Board 2");
        board2.setDescription("Desc 1");
        board2.setProject(project);

        boardRepository.save(board1);
        boardRepository.save(board2);

        mockMvc.perform(get("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Board 1"))
                .andExpect(jsonPath("$[1].name").value("Board 2"))
                .andExpect(jsonPath("$[0].projectId").value(project.getId()))
                .andExpect(jsonPath("$[1].projectId").value(project.getId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetBoardById() throws Exception {

        Project project = new Project();
        project.setName("Project");
        project.setDescription("Desc");
        project = projectRepository.save(project);

        Board board = new Board();
        board.setName("Board 1");
        board.setDescription("Desc");
        board.setProject(project);
        board = boardRepository.save(board);

        Long id = board.getId();

        mockMvc.perform(get("/api/v1/boards/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Board 1"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.projectId").value(project.getId()));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testUpdateBoard() throws Exception {

        Project p1 = new Project();
        p1.setName("Old project");
        p1.setDescription("Old Description");
        p1 = projectRepository.save(p1);

        Project p2 = new Project();
        p2.setName("New project");
        p2.setDescription("New Description");
        p2 = projectRepository.save(p2);

        Board board1 = new Board();
        board1.setName("Old board");
        board1.setDescription("Old Description");
        board1.setProject(p1);
        board1 = boardRepository.save(board1);

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("New board");
        dto.setDescription("Old Description");
        dto.setProjectId(p2.getId());

        Long id = board1.getId();

        mockMvc.perform(put("/api/v1/boards/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Board updated successfully"));

    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testDeleteBoard() throws Exception {

        Project project = new Project();
        project.setName("Project");
        project.setDescription("Desc");
        project = projectRepository.save(project);

        Board board = new Board();
        board.setName("Board 1");
        board.setDescription("Desc");
        board.setProject(project);
        board = boardRepository.save(board);

        Long id = board.getId();

        mockMvc.perform(delete("/api/v1/boards/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Board deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testBoardNotFound() throws Exception {

        Long invalidId = 999L;

        mockMvc.perform(get("/api/v1/boards/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("board_not_found"))
                .andExpect(jsonPath("$.status").value(404));
    }
}

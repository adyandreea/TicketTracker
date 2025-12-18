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
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.projectId").value(project.getId()));
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
        dto.setDescription("New Description");
        dto.setProjectId(p2.getId());

        Long id = board1.getId();

        mockMvc.perform(put("/api/v1/boards/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.name").value("New board"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.projectId").value(p2.getId()));

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

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testValidationOnCreateBoard() throws Exception {

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("");
        dto.setDescription("a".repeat(300));
        dto.setProjectId(null);

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", org.hamcrest.Matchers.hasSize(4)))

                .andExpect(jsonPath("$.fieldErrors[?(@.field=='name')].message",
                        org.hamcrest.Matchers.hasItem("name_is_required")))
                .andExpect(jsonPath("$.fieldErrors[?(@.field=='name')].message",
                        org.hamcrest.Matchers.hasItem("name_length_invalid")))

                .andExpect(jsonPath("$.fieldErrors[?(@.field=='description')].message",
                        org.hamcrest.Matchers.hasItem("Description too long")))

                .andExpect(jsonPath("$.fieldErrors[?(@.field=='projectId')].message",
                        org.hamcrest.Matchers.hasItem("ProjectId cannot be null")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testGetBoardsByProjectId() throws Exception{

        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setDescription("Desc");
        project1 = projectRepository.save(project1);

        Board board1 = new Board();
        board1.setName("Board 1");
        board1.setDescription("Desc");
        board1.setProject(project1);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setName("Board 2");
        board2.setDescription("Desc");
        board2.setProject(project1);
        boardRepository.save(board2);

        Long id = project1.getId();

        mockMvc.perform(get("/api/v1/boards/by-project/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Board 1"))
                .andExpect(jsonPath("$[1].name").value("Board 2"))
                .andExpect(jsonPath("$[0].projectId").value(id))
                .andExpect(jsonPath("$[1].projectId").value(id));
    }
}

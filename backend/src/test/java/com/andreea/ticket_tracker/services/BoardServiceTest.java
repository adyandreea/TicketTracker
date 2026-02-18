package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.security.config.ProjectSecurityEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectSecurityEvaluator projectSecurity;

    @InjectMocks
    private BoardService boardService;

    private void mockSecurityContext(String username, boolean isAdmin) {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(auth.getName()).thenReturn(username);
        when(projectSecurity.isUserAdmin()).thenReturn(isAdmin);
    }

    @Test
    void testCreateBoard(){
        Project project = new Project();
        project.setId(1L);

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("Test");
        dto.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(boardRepository.save(any(Board.class))).thenAnswer(i -> i.getArguments()[0]);

        boardService.createBoard(dto);
        verify(projectSecurity).validateUserAccess(project);
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void testGetAllBoards(){
        mockSecurityContext("adminUser", true);

        Board b1 = new Board();
        when(boardRepository.findAll()).thenReturn(List.of(b1));

        var result = boardService.getAllBoards();

        assertEquals(1, result.size());
        verify(boardRepository).findAll();
    }

    @Test
    void testGetBoardById(){
        Project project = new Project();
        Board board = new Board();
        board.setProject(project);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        boardService.getBoard(1L);

        verify(projectSecurity).validateUserAccess(project);
    }

    @Test
    void testUpdateBoard() {
        Project project1 = new Project();
        project1.setId(1L);
        Board board = new Board();
        board.setProject(project1);

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("New board");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(boardRepository.save(any(Board.class))).thenAnswer(i -> i.getArguments()[0]);

        boardService.updateBoard(1L, dto);

        verify(projectSecurity).validateUserAccess(project1);
        verify(boardRepository).save(board);
    }

    @Test
    void testGetBoardsByProjectId(){
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        Board b1 = new Board(); b1.setProject(project);
        mockSecurityContext("adminUser", true);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(boardRepository.findByProjectId(projectId)).thenReturn(List.of(b1));

        List<BoardResponseDTO> result = boardService.getBoardsByProjectId(projectId);

        assertEquals(1, result.size());
        verify(boardRepository).findByProjectId(projectId);
    }
}

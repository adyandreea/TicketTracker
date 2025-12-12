package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.exceptions.BoardNotFoundException;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void testCreateBoard(){
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("Test");
        dto.setDescription("Desc");
        dto.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        boardService.createBoard(dto);

        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void testGetAllBoards(){
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Desc");

        Board board1 = new Board();
        board1.setId(1L);
        board1.setName("Board 1");
        board1.setDescription("Desc");
        board1.setProject(project);

        Board board2 = new Board();
        board2.setId(1L);
        board2.setName("Board 2");
        board2.setDescription("Desc");
        board2.setProject(project);

        when(boardRepository.findAll()).thenReturn(List.of(board1, board2));

        var result = boardService.getAllBoards();

        assertEquals(2,result.size());
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    void testGetBoardById(){
        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Desc");

        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");
        board.setProject(project);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        var result = boardService.getBoard(1L);

        assertEquals("Board 1", result.getName());
        assertEquals("Desc", result.getDescription());
        assertEquals(1L, result.getProjectId());
        verify(boardRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBoardByIdNotFound() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BoardNotFoundException.class, () -> boardService.getBoard(1L));
    }

    @Test
    void testUpdateBoard() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Old project");
        project1.setDescription("Old desc");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("New project");
        project2.setDescription("New desc");

        Board board = new Board();
        board.setId(1L);
        board.setName("Old board");
        board.setDescription("Old desc");
        board.setProject(project1);

        BoardRequestDTO dto = new BoardRequestDTO();
        dto.setName("New board");
        dto.setDescription("New desc");
        dto.setProjectId(2L);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(projectRepository.findById(2L)).thenReturn(Optional.of(project2));

        boardService.updateBoard(1L, dto);
        verify(boardRepository, times(1)).save(board);
        assertEquals("New board", board.getName());
        assertEquals("New desc", board.getDescription());
        assertEquals(2L,board.getProject().getId());
    }

    @Test
    void testDeleteBoard() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");
        project.setDescription("Desc");

        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");
        board.setProject(project);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        boardService.deleteBoard(1L);

        verify(boardRepository, times(1)).deleteById(1L);
    }

    @Test
    void getBoardsByProjectId(){

        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);
        project.setName("Project 1");

        Board board1 = new Board();
        board1.setId(10L);
        board1.setName("Board 1");
        board1.setProject(project);

        Board board2 = new Board();
        board2.setId(10L);
        board2.setName("Board 2");
        board2.setProject(project);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(boardRepository.findByProjectId(projectId)).thenReturn(List.of(board1, board2));

        List<BoardResponseDTO> result = boardService.getBoardsByProjectId(projectId);

        assertEquals(2, result.size());
        assertEquals("Board 1", result.get(0).getName());
        assertEquals("Board 2", result.get(1).getName());
        assertEquals(projectId, result.get(0).getProjectId());

        verify(projectRepository, times(1)).findById(projectId);
        verify(boardRepository, times(1)).findByProjectId(projectId);
        verify(boardRepository, times(0)).findAll();
    }
}

package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.exceptions.BoardNotFoundException;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;
import com.andreea.ticket_tracker.mapper.BoardDTOMapper;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.ProjectRepository;
import com.andreea.ticket_tracker.security.config.ProjectSecurityEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSecurityEvaluator projectSecurity;

    @Autowired
    public BoardService(BoardRepository boardRepository, ProjectRepository projectRepository, ProjectSecurityEvaluator projectSecurity) {
        this.boardRepository = boardRepository;
        this.projectRepository = projectRepository;
        this.projectSecurity = projectSecurity;
    }

    public BoardResponseDTO createBoard(BoardRequestDTO dto){
      Project project = projectRepository.findById(dto.getProjectId())
              .orElseThrow(ProjectNotFoundException::new);

      projectSecurity.validateUserAccess(project);
      Board board = BoardDTOMapper.toEntity(dto, project);

      Board savedBoard = boardRepository.save(board);
      return BoardDTOMapper.toDTO(savedBoard);
    }

    public List<BoardResponseDTO> getAllBoards() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Board> boards = projectSecurity.isUserAdmin()
                ? boardRepository.findAll()
                : boardRepository.findAllByUser(username);

        return boards.stream().map(BoardDTOMapper::toDTO).toList();
    }

    public BoardResponseDTO getBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        projectSecurity.validateUserAccess(board.getProject());

        return BoardDTOMapper.toDTO(board);
    }

    public BoardResponseDTO updateBoard(Long id, BoardRequestDTO dto){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        projectSecurity.validateUserAccess(board.getProject());

        board.setName(dto.getName());
        board.setDescription(dto.getDescription());

        if(dto.getProjectId() != null){
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(ProjectNotFoundException::new);
            board.setProject(project);
        }

        Board savedBoard = boardRepository.save(board);
        return BoardDTOMapper.toDTO(savedBoard);
    }

    public void deleteBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        projectSecurity.validateUserAccess(board.getProject());
        boardRepository.deleteById(id);
    }

    public List<BoardResponseDTO> getBoardsByProjectId(Long projectId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        projectRepository.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        List<Board> boards = projectSecurity.isUserAdmin()
                ? boardRepository.findByProjectId(projectId)
                : boardRepository.findAllByProjectAndUser(projectId, username);

        return boards.stream().map(BoardDTOMapper::toDTO).toList();
    }
}

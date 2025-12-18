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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, ProjectRepository projectRepository) {
        this.boardRepository = boardRepository;
        this.projectRepository = projectRepository;
    }

    public BoardResponseDTO createBoard(BoardRequestDTO dto){
      Project project = projectRepository.findById(dto.getProjectId())
              .orElseThrow(ProjectNotFoundException::new);

      Board board = BoardDTOMapper.toEntity(dto, project);

      Board savedBoard = boardRepository.save(board);
      return BoardDTOMapper.toDTO(savedBoard);
    }

    public List<BoardResponseDTO> getAllBoards(){
        return boardRepository.findAll()
                .stream()
                .map(BoardDTOMapper::toDTO)
                .toList();
    }

    public BoardResponseDTO getBoard(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        return BoardDTOMapper.toDTO(board);
    }

    public BoardResponseDTO updateBoard(Long id, BoardRequestDTO dto){
        Board board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

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
        boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        boardRepository.deleteById(id);
    }

    public List<BoardResponseDTO> getBoardsByProjectId(Long projectId){
        projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return boardRepository.findByProjectId(projectId)
                .stream()
                .map(BoardDTOMapper::toDTO)
                .toList();
    }
}

package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;

/**
 * Mapper utility to convert between Board entities and DTOs.
 */
public class BoardDTOMapper {

    /**
     * Converts a BoardRequestDTO to a Board entity.
     * @param dto the request data transfer object
     * @param project the parent project to be associated with the board
     * @return a new Board entity
     */
    public static Board toEntity(BoardRequestDTO dto, Project project){
        Board board = new Board();
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());
        board.setProject(project);
        return board;
    }

    /**
     * Converts a Board entity to a BoardResponseDTO.
     * @param board the entity to convert
     * @return the populated response DTO
     */
    public static BoardResponseDTO toDTO(Board board){
        BoardResponseDTO dto = new BoardResponseDTO();

        dto.setId(board.getId());
        dto.setName(board.getName());
        dto.setDescription(board.getDescription());

        if(board.getProject() != null){
            dto.setProjectId(board.getProject().getId());
            dto.setProjectName(board.getProject().getName());
        }

        dto.setTicketCount(board.getTickets() != null ? board.getTickets().size() : 0);

        return dto;
    }
}

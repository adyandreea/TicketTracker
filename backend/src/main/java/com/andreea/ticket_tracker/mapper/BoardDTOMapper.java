package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.BoardRequestDTO;
import com.andreea.ticket_tracker.dto.response.BoardResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.exceptions.ProjectNotFoundException;

public class BoardDTOMapper {

    public static Board toEntity(BoardRequestDTO dto, Project project){
        Board board = new Board();
        board.setName(dto.getName());
        board.setDescription(dto.getDescription());
        board.setProject(project);
        return board;
    }

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

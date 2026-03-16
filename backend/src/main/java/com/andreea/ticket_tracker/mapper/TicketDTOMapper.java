package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;

/**
 * Mapper utility to convert between Ticket entities and DTOs.
 */
public class TicketDTOMapper {

    /**
     * Converts a TicketRequestDTO to a Ticket entity.
     * @param dto the request data transfer object
     * @param board the parent board to be associated with the ticket
     * @return a new Ticket entity
     */
    public static Ticket toEntity(TicketRequestDTO dto, Board board){
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPosition(dto.getPosition());
        ticket.setStatus(dto.getStatus());
        ticket.setBoard(board);
        ticket.setStoryPoints(dto.getStoryPoints());
        return ticket;
    }

    /**
     * Converts a Ticket entity to a TicketResponseDTO.
     * @param ticket the entity to convert
     * @return the populated response DTO
     */
    public static TicketResponseDTO toDTO(Ticket ticket){
        TicketResponseDTO dto = new TicketResponseDTO();

        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPosition(ticket.getPosition());
        dto.setStoryPoints(ticket.getStoryPoints());

        if(ticket.getBoard() != null){
            dto.setBoardId(ticket.getBoard().getId());
            dto.setBoardName(ticket.getBoard().getName());

            if(ticket.getBoard().getProject() != null) {
                dto.setProjectId(ticket.getBoard().getProject().getId());
            }
        }

        if(ticket.getAssignedUser() != null){
            dto.setAssignedUserId(ticket.getAssignedUser().getId());
            dto.setAssignedUsername(ticket.getAssignedUser().getUsername());
        }
        return dto;
    }
}

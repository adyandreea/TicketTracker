package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;

public class TicketDTOMapper {

    public static Ticket toEntity(TicketRequestDTO dto, Board board){
        Ticket ticket = new Ticket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPosition(dto.getPosition());
        ticket.setStatus(dto.getStatus());
        ticket.setBoard(board);
        return ticket;
    }

    public static TicketResponseDTO toDTO(Ticket ticket){
        TicketResponseDTO dto = new TicketResponseDTO();

        dto.setId(ticket.getId());
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPosition(ticket.getPosition());

        if(ticket.getBoard() != null){
            dto.setBoardId(ticket.getBoard().getId());
            dto.setBoardName(ticket.getBoard().getName());
        }

        return dto;
    }
}

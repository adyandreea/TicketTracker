package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketDTOMapperTest {

    @Test
    void testToEntity() {

        Board board = new Board();
        board.setId(1L);
        board.setName("Board");
        board.setDescription("Desc");

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("Ticket");
        dto.setDescription("Desc");
        dto.setPosition(1);
        dto.setBoardId(board.getId());

        Ticket ticket = TicketDTOMapper.toEntity(dto, board);

        assertThat(board).isNotNull();
        assertThat(ticket).isNotNull();
        assertThat(ticket.getTitle()).isEqualTo(dto.getTitle());
        assertThat(ticket.getDescription()).isEqualTo(dto.getDescription());
        assertThat(ticket.getPosition()).isEqualTo(dto.getPosition());
        assertThat(board.getId()).isEqualTo(dto.getBoardId());
    }

    @Test
    void testToDTO() {

        Board board = new Board();
        board.setId(1L);
        board.setName("Board");
        board.setDescription("Desc");

        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket");
        ticket.setDescription("Desc");
        ticket.setPosition(1);
        ticket.setBoard(board);

        TicketResponseDTO dto = TicketDTOMapper.toDTO(ticket);

        assertThat(ticket).isNotNull();
        assertThat(board).isNotNull();
        assertThat(dto.getTitle()).isEqualTo(ticket.getTitle());
        assertThat(dto.getDescription()).isEqualTo(ticket.getDescription());
        assertThat(dto.getPosition()).isEqualTo(ticket.getPosition());
        assertThat(dto.getBoardId()).isEqualTo(board.getId());
    }
}

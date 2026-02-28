package com.andreea.ticket_tracker.mapper;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.entity.User;
import org.junit.jupiter.api.Test;

import static com.andreea.ticket_tracker.entity.TicketStatus.TODO;
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
        dto.setStatus(TODO);
        dto.setBoardId(board.getId());
        dto.setStoryPoints(8);

        Ticket ticket = TicketDTOMapper.toEntity(dto, board);

        assertThat(board).isNotNull();
        assertThat(ticket).isNotNull();
        assertThat(ticket.getTitle()).isEqualTo(dto.getTitle());
        assertThat(ticket.getDescription()).isEqualTo(dto.getDescription());
        assertThat(ticket.getPosition()).isEqualTo(dto.getPosition());
        assertThat(ticket.getStatus()).isEqualTo(dto.getStatus());
        assertThat(board.getId()).isEqualTo(dto.getBoardId());
        assertThat(ticket.getStoryPoints()).isEqualTo(dto.getStoryPoints());
    }

    @Test
    void testToDTO() {

        Board board = new Board();
        board.setId(1L);
        board.setName("Board");
        board.setDescription("Desc");

        User user = new User();
        user.setId(55L);
        user.setUsername("andreea_dev");

        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket");
        ticket.setDescription("Desc");
        ticket.setPosition(1);
        ticket.setStatus(TODO);
        ticket.setBoard(board);
        ticket.setStoryPoints(5);
        ticket.setAssignedUser(user);

        TicketResponseDTO dto = TicketDTOMapper.toDTO(ticket);

        assertThat(ticket).isNotNull();
        assertThat(board).isNotNull();
        assertThat(dto.getTitle()).isEqualTo(ticket.getTitle());
        assertThat(dto.getDescription()).isEqualTo(ticket.getDescription());
        assertThat(dto.getPosition()).isEqualTo(ticket.getPosition());
        assertThat(dto.getStatus()).isEqualTo(ticket.getStatus());
        assertThat(dto.getBoardId()).isEqualTo(board.getId());
        assertThat(dto.getStoryPoints()).isEqualTo(ticket.getStoryPoints());
        assertThat(dto.getAssignedUserId()).isEqualTo(user.getId());
    }
}

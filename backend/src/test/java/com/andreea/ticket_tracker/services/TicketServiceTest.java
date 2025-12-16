package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.exceptions.TicketNotFoundException;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.andreea.ticket_tracker.entity.TicketStatus.DONE;
import static com.andreea.ticket_tracker.entity.TicketStatus.TODO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {


    @Mock
    private TicketRepository ticketRepository;

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void testCreateTicket(){
        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("Test");
        dto.setDescription("Desc");
        dto.setPosition(1);
        dto.setStatus(TODO);
        dto.setBoardId(1L);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        ticketService.createTicket(dto);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testGetAllTickets(){
        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setTitle("Ticket 1");
        ticket1.setDescription("Desc");
        ticket1.setPosition(1);
        ticket1.setStatus(TODO);
        ticket1.setBoard(board);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setTitle("Ticket 1");
        ticket2.setDescription("Desc");
        ticket2.setPosition(2);
        ticket2.setStatus(DONE);
        ticket2.setBoard(board);

        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2));

        var result = ticketService.getAllTickets();

        assertEquals(2,result.size());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetTicketById(){
        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Ticket 1");
        ticket.setDescription("Desc");
        ticket.setPosition(1);
        ticket.setBoard(board);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        var result = ticketService.getTicket(1L);

        assertEquals("Ticket 1", result.getTitle());
        assertEquals("Desc", result.getDescription());
        assertEquals(1,result.getPosition());
        assertEquals(1L, result.getBoardId());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketByIdNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicket(1L));
    }

    @Test
    void testUpdateTicket() {
        Board board1 = new Board();
        board1.setId(1L);
        board1.setName("Old board");
        board1.setDescription("Old desc");

        Board board2 = new Board();
        board2.setId(2L);
        board2.setName("New board");
        board2.setDescription("New desc");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Old ticket");
        ticket.setDescription("Old desc");
        ticket.setPosition(1);
        ticket.setStatus(TODO);
        ticket.setBoard(board1);

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("New ticket");
        dto.setDescription("New desc");
        dto.setPosition(2);
        dto.setStatus(DONE);
        dto.setBoardId(2L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(boardRepository.findById(2L)).thenReturn(Optional.of(board2));

        ticketService.updateTicket(1L, dto);
        verify(ticketRepository, times(1)).save(ticket);
        assertEquals("New ticket", ticket.getTitle());
        assertEquals("New desc", ticket.getDescription());
        assertEquals(2,ticket.getPosition());
        assertEquals(DONE,ticket.getStatus());
        assertEquals(2L,ticket.getBoard().getId());
    }

    @Test
    void testDeleteTicket() {

        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Ticket 1");
        ticket.setDescription("Desc");
        ticket.setStatus(TODO);
        ticket.setBoard(board);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTicketsByBoardId(){

        Board board = new Board();
        board.setId(1L);
        board.setName("Board 1");
        board.setDescription("Desc");

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setTitle("Ticket 1");
        ticket1.setDescription("Desc");
        ticket1.setPosition(1);
        ticket1.setStatus(TODO);
        ticket1.setBoard(board);

        Ticket ticket2 = new Ticket();
        ticket2.setId(1L);
        ticket2.setTitle("Ticket 2");
        ticket2.setDescription("Desc");
        ticket1.setPosition(2);
        ticket1.setStatus(DONE);
        ticket2.setBoard(board);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(ticketRepository.findByBoardId(1L)).thenReturn(List.of(ticket1, ticket2));

        List<TicketResponseDTO> result = ticketService.getTicketsByBoardId(1L);

        assertEquals(2, result.size());
        assertEquals("Ticket 1", result.get(0).getTitle());
        assertEquals("Ticket 2", result.get(1).getTitle());
        assertEquals(1L, result.get(0).getBoardId());

        verify(boardRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).findByBoardId(1L);
        verify(ticketRepository, times(1)).findByBoardId(1L);
    }
}

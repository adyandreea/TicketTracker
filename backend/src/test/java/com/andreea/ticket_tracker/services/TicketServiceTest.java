package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Project;
import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.TicketRepository;
import com.andreea.ticket_tracker.security.config.ProjectSecurityEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {


    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ProjectSecurityEvaluator projectSecurity;

    @InjectMocks
    private TicketService ticketService;

    private void mockSecurityContext(String username, boolean isAdmin) {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(auth.getName()).thenReturn(username);
        when(projectSecurity.isUserAdmin()).thenReturn(isAdmin);
    }

    @Test
    void testCreateTicket(){
        Project project = new Project();
        Board board = new Board();
        board.setId(1L);
        board.setProject(project);

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("Test");
        dto.setBoardId(1L);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        var result = ticketService.createTicket(dto);

        verify(projectSecurity).validateUserAccess(project);
        assertEquals("Test", result.getTitle());
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testGetAllTickets(){
        mockSecurityContext("user1", false);

        Ticket ticket = new Ticket();
        when(ticketRepository.findAllByBoard_Project_Users_Username("user1")).thenReturn(List.of(ticket));

        var result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        verify(ticketRepository).findAllByBoard_Project_Users_Username("user1");
    }

    @Test
    void testGetTicketById(){
        Project project = new Project();
        Board board = new Board();
        board.setProject(project);
        Ticket ticket = new Ticket();
        ticket.setTitle("Ticket 1");
        ticket.setBoard(board);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        var result = ticketService.getTicket(1L);

        verify(projectSecurity).validateUserAccess(project);
        assertEquals("Ticket 1", result.getTitle());
    }

    @Test
    void testUpdateTicket() {
        Project project = new Project();
        Board board = new Board();
        board.setProject(project);
        Ticket ticket = new Ticket();
        ticket.setBoard(board);

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("New title");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        ticketService.updateTicket(1L, dto);

        verify(projectSecurity).validateUserAccess(project);
        assertEquals("New title", ticket.getTitle());
    }

    @Test
    void testDeleteTicket() {
        Project project = new Project();
        Board board = new Board();
        board.setProject(project);
        Ticket ticket = new Ticket();
        ticket.setBoard(board);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(1L);

        verify(projectSecurity).validateUserAccess(project);
        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void testGetTicketsByBoardId_AsUser(){
        mockSecurityContext("user1", false);
        Project project = new Project();
        Board board = new Board();
        board.setProject(project);

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(ticketRepository.findAllByBoardIdAndBoard_Project_Users_Username(1L, "user1"))
                .thenReturn(List.of(new Ticket()));

        var result = ticketService.getTicketsByBoardId(1L);

        assertEquals(1, result.size());
        verify(ticketRepository).findAllByBoardIdAndBoard_Project_Users_Username(1L, "user1");
    }
}

package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.entity.Board;
import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.exceptions.BoardNotFoundException;
import com.andreea.ticket_tracker.exceptions.TicketNotFoundException;
import com.andreea.ticket_tracker.mapper.TicketDTOMapper;
import com.andreea.ticket_tracker.repository.BoardRepository;
import com.andreea.ticket_tracker.repository.TicketRepository;
import com.andreea.ticket_tracker.security.config.ProjectSecurityEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BoardRepository boardRepository;
    private final ProjectSecurityEvaluator projectSecurity;

    @Autowired
    public TicketService(TicketRepository ticketRepository, BoardRepository boardRepository, ProjectSecurityEvaluator projectSecurity) {
        this.ticketRepository = ticketRepository;
        this.boardRepository = boardRepository;
        this.projectSecurity = projectSecurity;
    }

    public TicketResponseDTO createTicket(TicketRequestDTO dto){
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        projectSecurity.validateUserAccess(board.getProject());
        Ticket ticket = TicketDTOMapper.toEntity(dto, board);
        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketDTOMapper.toDTO(savedTicket);
    }

    public List<TicketResponseDTO> getAllTickets(){
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();

        List<Ticket> tickets = projectSecurity.isUserAdmin()
                ? ticketRepository.findAll()
                : ticketRepository.findAllByBoard_Project_Users_Username(username);

        return tickets.stream().map(TicketDTOMapper::toDTO).toList();
    }

    public TicketResponseDTO getTicket(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        projectSecurity.validateUserAccess(ticket.getBoard().getProject());
        return TicketDTOMapper.toDTO(ticket);
    }

    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO dto){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        projectSecurity.validateUserAccess(ticket.getBoard().getProject());

        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPosition(dto.getPosition());

        if (dto.getStatus() != null) {
            ticket.setStatus(dto.getStatus());
        }

        if(dto.getBoardId() != null){
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(BoardNotFoundException::new);

            projectSecurity.validateUserAccess(board.getProject());
            ticket.setBoard(board);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketDTOMapper.toDTO(savedTicket);
    }

    public void deleteTicket(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        projectSecurity.validateUserAccess(ticket.getBoard().getProject());
        ticketRepository.deleteById(id);
    }

    public List<TicketResponseDTO> getTicketsByBoardId(Long boardId){
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<Ticket> tickets;
        if (projectSecurity.isUserAdmin()) {
            tickets = ticketRepository.findByBoardId(boardId);
        } else {
            tickets = ticketRepository.findAllByBoardIdAndBoard_Project_Users_Username(boardId, username);
        }

        return tickets.stream().map(TicketDTOMapper::toDTO).toList();
    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, BoardRepository boardRepository) {
        this.ticketRepository = ticketRepository;
        this.boardRepository = boardRepository;
    }

    public void createTicket(TicketRequestDTO dto){
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Ticket ticket = TicketDTOMapper.toEntity(dto, board);
        Ticket saved = ticketRepository.save(ticket);
        TicketDTOMapper.toDTO(saved);
    }

    public List<TicketResponseDTO> getAllTickets(){
        return ticketRepository.findAll()
                .stream()
                .map(TicketDTOMapper::toDTO)
                .toList();
    }

    public TicketResponseDTO getTicket(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        return TicketDTOMapper.toDTO(ticket);
    }

    public void updateTicket(Long id, TicketRequestDTO dto){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPosition(dto.getPosition());

        if(dto.getBoardId() != null){
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(BoardNotFoundException::new);
            ticket.setBoard(board);
        }

        Ticket updated = ticketRepository.save(ticket);
        TicketDTOMapper.toDTO(updated);
    }

    public void deleteTicket(Long id){
        ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        ticketRepository.deleteById(id);
    }
}

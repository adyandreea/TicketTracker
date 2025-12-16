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

    public TicketResponseDTO createTicket(TicketRequestDTO dto){
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(BoardNotFoundException::new);

        Ticket ticket = TicketDTOMapper.toEntity(dto, board);
        Ticket savedTicket = ticketRepository.save(ticket);
        ticketRepository.save(ticket);
        return TicketDTOMapper.toDTO(savedTicket);
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

        if (dto.getStatus() != null) {
            ticket.setStatus(dto.getStatus());
        }

        if(dto.getBoardId() != null){
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(BoardNotFoundException::new);
            ticket.setBoard(board);
        }

        ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id){
        ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        ticketRepository.deleteById(id);
    }

    public List<TicketResponseDTO> getTicketsByBoardId(Long boardId){
        boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        return ticketRepository.findByBoardId(boardId)
                .stream()
                .map(TicketDTOMapper::toDTO)
                .toList();
    }
}

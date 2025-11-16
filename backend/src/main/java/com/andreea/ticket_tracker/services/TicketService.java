package com.andreea.ticket_tracker.services;

import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTicket(Ticket ticket){
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket getTicket(Long id){
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket updateTicket(Long id, Ticket newTicket){
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTitle(newTicket.getTitle());
                    ticket.setDescription(newTicket.getDescription());
                    ticket.setPosition(newTicket.getPosition());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not Found"));
    }

    public void deleteTicket(Long id){
        ticketRepository.deleteById(id);
    }
}

package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.entity.Ticket;
import com.andreea.ticket_tracker.services.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping("/ticket")
    public Ticket createticket(@RequestBody Ticket ticket){
        return ticketService.createTicket(ticket);
    }

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @GetMapping("/ticket/{id}")
    public Ticket getTicket(@PathVariable Long id){
        return ticketService.getTicket(id);
    }

    @PutMapping("/ticket/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket){
        return ticketService.updateTicket(id, ticket);
    }

    @DeleteMapping("/ticket/{id}")
    public String deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return "Ticket deleted: " + id;
    }
}

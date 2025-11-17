package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
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
    public TicketResponseDTO createticket(@RequestBody TicketRequestDTO dto){
        return ticketService.createTicket(dto);
    }

    @GetMapping("/tickets")
    public List<TicketResponseDTO> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @GetMapping("/ticket/{id}")
    public TicketResponseDTO getTicket(@PathVariable Long id){
        return ticketService.getTicket(id);
    }

    @PutMapping("/ticket/{id}")
    public TicketResponseDTO updateTicket(@PathVariable Long id, @RequestBody TicketRequestDTO dto){
        return ticketService.updateTicket(id, dto);
    }

    @DeleteMapping("/ticket/{id}")
    public String deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return "Ticket deleted: " + id;
    }
}

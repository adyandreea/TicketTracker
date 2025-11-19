package com.andreea.ticket_tracker.controllers;

import com.andreea.ticket_tracker.dto.request.TicketRequestDTO;
import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import com.andreea.ticket_tracker.dto.response.TicketResponseDTO;
import com.andreea.ticket_tracker.handler.ResponseHandler;
import com.andreea.ticket_tracker.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService){
        this.ticketService = ticketService;
    }

    @PostMapping("/ticket")
    public ResponseEntity<SuccessDTO> createTicket(@Valid @RequestBody TicketRequestDTO dto){
        ticketService.createTicket(dto);
        return ResponseHandler.created("Ticket created successfully");
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
    public ResponseEntity<SuccessDTO> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketRequestDTO dto){
        ticketService.updateTicket(id, dto);
        return ResponseHandler.updated("Ticket updated successfully");
    }

    @DeleteMapping("/ticket/{id}")
    public ResponseEntity<SuccessDTO> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseHandler.deleted("Ticket deleted successfully");
    }
}

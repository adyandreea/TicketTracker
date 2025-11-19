package com.andreea.ticket_tracker.exceptions;

public class TicketNotFoundException extends RuntimeException{

    public TicketNotFoundException(){
        super("ticket_not_found");
    }
}

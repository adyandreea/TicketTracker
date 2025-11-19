package com.andreea.ticket_tracker.exceptions;

public class BoardNotFoundException extends RuntimeException{

    public BoardNotFoundException(){
        super("board_not_found");
    }
}

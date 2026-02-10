package com.andreea.ticket_tracker.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("user_not_found");
    }
}

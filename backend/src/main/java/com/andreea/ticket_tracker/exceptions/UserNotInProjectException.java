package com.andreea.ticket_tracker.exceptions;

public class UserNotInProjectException extends RuntimeException {
    public UserNotInProjectException(){
        super("user_is_not_in_project");
    }
}

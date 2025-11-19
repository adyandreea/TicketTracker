package com.andreea.ticket_tracker.exceptions;

public class ProjectNotFoundException extends RuntimeException{

    public ProjectNotFoundException(){
        super("project_not_found");
    }
}

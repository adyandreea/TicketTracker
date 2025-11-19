package com.andreea.ticket_tracker.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFieldError {
    private String field;
    private String message;
}

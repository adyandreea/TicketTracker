package com.andreea.ticket_tracker.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FieldErrorResponse {
    private List<CustomFieldError> fieldErrors;
}

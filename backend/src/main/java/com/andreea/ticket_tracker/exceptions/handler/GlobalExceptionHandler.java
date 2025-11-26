package com.andreea.ticket_tracker.exceptions.handler;

import com.andreea.ticket_tracker.dto.response.ErrorDTO;
import com.andreea.ticket_tracker.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProjectNotFound(ProjectNotFoundException ex) {
        ErrorDTO error = new ErrorDTO();

        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleBoardNotFound(BoardNotFoundException ex){
        ErrorDTO error = new ErrorDTO();

        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleTicketNotFound(TicketNotFoundException ex){
        ErrorDTO error = new ErrorDTO();

        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<FieldErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();
        List<CustomFieldError> fieldErrors = new ArrayList<>();

        ex.getAllErrors().forEach(error -> {
            CustomFieldError fieldError = new CustomFieldError();
            fieldError.setField(((FieldError) error).getField());
            fieldError.setMessage(error.getDefaultMessage());
            fieldErrors.add(fieldError);
        });

        fieldErrorResponse.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(fieldErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
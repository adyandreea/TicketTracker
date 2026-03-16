package com.andreea.ticket_tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Standard DTO for returning error details to the client.
 */
@Getter
@Setter
public class ErrorDTO {

    /**
     * HTTP status code of the error.
     */
    private int status;

    /**
     * The message explaining the error.
     */
    private String message;

    /**
     * Time when the error occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
}

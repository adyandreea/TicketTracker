package com.andreea.ticket_tracker.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Standard DTO for returning success messages and status codes.
 */
@Getter
@Setter
public class SuccessDTO {

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Success message for the user interface.
     */
    private String message;

    /**
     * Helper method to create a new success response.
     * * @param status the HTTP status
     * @param message the message to return
     * @return a new SuccessDTO instance
     */
    public static SuccessDTO returnNewDTO(int status, String message){

        SuccessDTO successDTO = new SuccessDTO();
        successDTO.setStatus(status);
        successDTO.setMessage(message);

        return successDTO;
    }
}

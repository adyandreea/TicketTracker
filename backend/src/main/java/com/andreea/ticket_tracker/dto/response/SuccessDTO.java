package com.andreea.ticket_tracker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessDTO {

    private int status;
    private String message;

    public static SuccessDTO returnNewDTO(int status, String message){

        SuccessDTO successDTO = new SuccessDTO();
        successDTO.setStatus(status);
        successDTO.setMessage(message);

        return successDTO;
    }
}

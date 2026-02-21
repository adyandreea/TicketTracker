package com.andreea.ticket_tracker.handler;

import com.andreea.ticket_tracker.dto.response.SuccessDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    private ResponseHandler(){}

    public static ResponseEntity<SuccessDTO> created(String message){
        return new ResponseEntity<>(SuccessDTO.returnNewDTO(HttpStatus.CREATED.value(),message),HttpStatus.CREATED);
    }

    public static ResponseEntity<SuccessDTO> updated(String message){
        return new ResponseEntity<>(SuccessDTO.returnNewDTO(HttpStatus.OK.value(),message),HttpStatus.OK);
    }

    public static ResponseEntity<SuccessDTO> deleted(String message){
        return new ResponseEntity<>(SuccessDTO.returnNewDTO(HttpStatus.OK.value(),message),HttpStatus.OK);
    }

    public static ResponseEntity<SuccessDTO> success(String message){
        return new ResponseEntity<>(SuccessDTO.returnNewDTO(HttpStatus.OK.value(),message),HttpStatus.OK);
    }
}

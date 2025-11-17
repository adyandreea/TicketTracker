package com.andreea.ticket_tracker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {
    private String title;
    private String description;
    private int position;
    private Long boardId;
}

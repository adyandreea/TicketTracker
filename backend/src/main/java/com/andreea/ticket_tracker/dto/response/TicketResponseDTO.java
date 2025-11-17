package com.andreea.ticket_tracker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private Long id;
    private String title;
    private String description;
    private int position;
    private Long boardId;
    private String boardName;
}

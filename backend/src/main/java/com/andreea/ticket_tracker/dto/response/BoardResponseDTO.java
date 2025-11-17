package com.andreea.ticket_tracker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private String projectName;
    private int ticketCount;
}

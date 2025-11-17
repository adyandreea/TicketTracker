package com.andreea.ticket_tracker.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {
    private String name;
    private String description;
    private Long projectId;
}

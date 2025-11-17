package com.andreea.ticket_tracker.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private int boardCount;
}

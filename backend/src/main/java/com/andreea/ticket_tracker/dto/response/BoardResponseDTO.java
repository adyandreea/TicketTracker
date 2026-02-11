package com.andreea.ticket_tracker.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDTO {

    private Long id;

    @NotBlank(message = "name_required")
    @Size(max = 64, min = 1, message = "name_length_invalid")
    private String name;

    @Size(max = 255, message = "description_too_long")
    private String description;

    @NotNull(message = "project_id_required")
    private Long projectId;

    @NotBlank(message = "project_name_required")
    private String projectName;

    @Size(min = 0, message = "board_count_invalid")
    private int ticketCount;
}

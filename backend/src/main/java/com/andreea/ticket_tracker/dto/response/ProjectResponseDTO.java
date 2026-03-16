package com.andreea.ticket_tracker.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for project responses.
 */
@Getter
@Setter
public class ProjectResponseDTO {

    private Long id;

    /**
     * The name of the project.
     */
    @NotBlank(message = "name_required")
    @Size(max = 64, min = 1, message = "name_length_invalid")
    private String name;

    /**
     * Description of the project.
     */
    @Size(max = 255, message = "description_too_long")
    private String description;

    /**
     * Total number of boards associated with this project.
     */
    @Size(min = 0, message = "board_count_invalid")
    private int boardCount;
}

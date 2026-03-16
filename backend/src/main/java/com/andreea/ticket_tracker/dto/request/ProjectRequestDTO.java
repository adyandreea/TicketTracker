package com.andreea.ticket_tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for a project.
 */
@Getter
@Setter
public class ProjectRequestDTO {

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
}

package com.andreea.ticket_tracker.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for board responses.
 */
@Getter
@Setter
public class BoardResponseDTO {

    private Long id;

    /**
     * The name of the board.
     */
    @NotBlank(message = "name_required")
    @Size(max = 64, min = 1, message = "name_length_invalid")
    private String name;

    /**
     * Description of the board.
     */
    @Size(max = 255, message = "description_too_long")
    private String description;

    /**
     * Parent project of the board.
     */
    @NotNull(message = "project_id_required")
    private Long projectId;

    /**
     * The name of the parent project.
     */
    @NotBlank(message = "project_name_required")
    private String projectName;

    /**
     * Total number of tickets associated with this board.
     */
    @Size(min = 0, message = "board_count_invalid")
    private int ticketCount;
}

package com.andreea.ticket_tracker.dto.response;

import com.andreea.ticket_tracker.entity.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for ticket responses.
 */
@Getter
@Setter
public class TicketResponseDTO {
    private Long id;

    /**
     * The name of the ticket.
     */
    @NotBlank(message = "title_required")
    @Size(max = 64, min = 1, message = "title_length_invalid")
    private String title;

    /**
     * Description of the ticket.
     */
    @Size(max = 255, message = "description_too_long")
    private String description;

    /**
     * Position of the ticket.
     */
    @NotNull(message = "position_required")
    @Min(value = 0, message = "position_min_error")
    private Integer position;

    /**
     * Parent board of the ticket.
     */
    @NotNull(message = "board_id_required")
    private Long boardId;

    /**
     * Current status of the ticket.
     */
    @NotNull(message = "status_required")
    private TicketStatus status;

    /**
     * The name of the parent board.
     */
    @NotBlank(message = "board_name_required")
    private String boardName;

    /**
     * Story Points of the ticket.
     */
    private Integer storyPoints;

    /**
     * Member assigned to this ticket.
     */
    private Long assignedUserId;

    /**
     * Username of the member assigned to this ticket.
     */
    private String assignedUsername;

    /**
     * ID of the project this ticket belongs to.
     */
    private Long projectId;
}

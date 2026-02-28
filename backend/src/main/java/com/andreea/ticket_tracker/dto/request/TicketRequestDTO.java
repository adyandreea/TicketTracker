package com.andreea.ticket_tracker.dto.request;

import com.andreea.ticket_tracker.entity.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {

    @NotBlank(message = "title_required")
    @Size(max = 64, min = 1, message = "title_length_invalid")
    private String title;

    @Size(max = 255, message = "description_too_long")
    private String description;

    @NotNull(message = "position_required")
    @Min(value = 0, message = "position_min_error")
    private Integer position;

    @NotNull(message = "status_required")
    private TicketStatus status;

    @NotNull(message = "board_id_required")
    private Long boardId;

    private Integer storyPoints;
    private Long assignedUserId;
}

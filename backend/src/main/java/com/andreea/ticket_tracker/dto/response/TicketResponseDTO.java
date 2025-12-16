package com.andreea.ticket_tracker.dto.response;

import com.andreea.ticket_tracker.entity.TicketStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {
    private Long id;

    @NotBlank(message = "title_is_required")
    @Size(max = 64, min = 1, message = "title_length_invalid")
    private String title;

    @Size(max = 255, message = "Description too long")
    private String description;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be >= 0")
    private Integer position;

    @NotNull(message = "BoardId cannot be null")
    private Long boardId;

    @NotNull(message = "Status cannot be null")
    private TicketStatus status;

    @NotBlank(message = "board_name_cannot_be_null")
    private String boardName;
}

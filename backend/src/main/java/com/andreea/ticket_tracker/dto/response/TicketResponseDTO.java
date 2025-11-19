package com.andreea.ticket_tracker.dto.response;

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

    @NotNull(message = "BoardId cannot be null")
    private int position;

    @NotNull(message = "BoardId cannot be null")
    private Long boardId;

    @NotBlank(message = "board_name_cannot_be_null")
    private String boardName;
}

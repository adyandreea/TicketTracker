package com.andreea.ticket_tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {

    @NotBlank(message = "name_is_required")
    @Size(max = 64, min = 1, message = "name_length_invalid")
    private String name;

    @Size(max = 255, message = "Description too long")
    private String description;

    @NotNull(message = "ProjectId cannot be null")
    private Long projectId;
}

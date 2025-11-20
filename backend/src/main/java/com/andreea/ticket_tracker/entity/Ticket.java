package com.andreea.ticket_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity{

    @NotBlank(message = "title_is_required")
    @Size(max = 64, min = 1, message = "title_length_invalid")
    private String title;

    @Size(max = 255, message = "Description too long")
    private String description;

    @NotNull(message = "Position cannot be null")
    @Min(value = 0, message = "Position must be >= 0")
    private Integer position;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name="board_id")
    @NotNull(message = "BoardId cannot be null")
    private Board board;
}

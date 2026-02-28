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

    @NotBlank(message = "title_required")
    @Size(max = 50, min = 3, message = "title_length_invalid")
    private String title;

    @Size(max = 255, message = "description_too_long")
    private String description;

    @NotNull(message = "position_required")
    @Min(value = 0, message = "position_min_error")
    private Integer position;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name="board_id")
    @NotNull(message = "board_id_required")
    private Board board;

    @Min(value = 0, message = "story_points_min_error")
    private Integer storyPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;
}

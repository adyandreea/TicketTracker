package com.andreea.ticket_tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="boards")
public class Board extends BaseEntity {

    @NotBlank(message = "name_is_required")
    @Size(max = 64, min = 1, message = "name_length_invalid")
    private String name;

    @Size(max = 255, message = "Description too long")
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @NotNull(message = "ProjectId cannot be null")
    private Project project;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}

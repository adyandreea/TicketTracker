package com.andreea.ticket_tracker.entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="boards")
public class Board extends BaseEntity{

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @OneToMany(mappedBy="board", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}

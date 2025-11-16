package com.andreea.ticket_tracker.entity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="projects")
public class Project extends BaseEntity{

    private String name;
    private String description;


    @OneToMany(mappedBy="project", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Board> boards;


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


    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }
}

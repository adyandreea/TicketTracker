package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

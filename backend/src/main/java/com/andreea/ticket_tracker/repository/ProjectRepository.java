package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Set<Project> findAllByUsers_Id(Long userId);
}

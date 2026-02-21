package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.username = :username")
    List<Project> findAllByMember(String username);
}

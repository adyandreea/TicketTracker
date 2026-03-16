package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for Project entity operations.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Finds all projects where a user is a member.
     * @param username the username of the member
     * @return a list of projects associated with the user
     */
    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.username = :username")
    List<Project> findAllByMember(String username);
}

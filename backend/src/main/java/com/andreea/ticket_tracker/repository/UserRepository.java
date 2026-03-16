package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for User entity operations.
 */
public interface UserRepository extends JpaRepository<User, Long>{
    /**
     * Finds all users by username.
     * @param username the username of the user
     * @return an Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a username is taken.
     * @param username the username of the user
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Finds all users belonging to a specific project.
     * @param projectId the ID of the parent project
     * @return a set of users associated with the project
     */
    @Query("SELECT u FROM User u JOIN u.projects p WHERE p.id = :projectId")
    Set<User> findAllByProjectId(Long projectId);
}

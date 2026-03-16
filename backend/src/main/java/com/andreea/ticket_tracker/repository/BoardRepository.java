package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for Board entity operations.
 */
public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * Finds all boards belonging to a specific project.
     * @param projectId the ID of the parent project
     * @return a list of boards associated with the project
     */
    List<Board> findByProjectId(Long projectId);

    /**
     * Finds all boards from all projects where a user is a member.
     * @param username the username of the member
     * @return a list of boards accessible to the user
     */
    @Query("SELECT b FROM Board b JOIN b.project p JOIN p.users u WHERE u.username = :username")
    List<Board> findAllByUser(String username);

    /**
     * Finds all boards within a specific project, but only if the user is a member of that project.
     * @param projectId the ID of the project
     * @param username the username of the member
     * @return a list of boards in the project that the user can access
     */
    @Query("SELECT b FROM Board b JOIN b.project p JOIN p.users u " +
            "WHERE p.id = :projectId AND u.username = :username")
    List<Board> findAllByProjectAndUser(Long projectId, String username);
}

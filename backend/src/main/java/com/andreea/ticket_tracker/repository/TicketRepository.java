package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for Ticket entity operations.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds all tickets belonging to a specific board.
     * @param boardId the ID of the parent board
     * @return a list of tickets associated with the board
     */
    List<Ticket> findByBoardId(Long boardId);

    /**
     * Finds all tickets across all projects where a user is a member.
     * @param username the username of the member
     * @return a list of tickets accessible to the user
     */
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.board b " +
            "JOIN b.project p " +
            "JOIN p.users u " +
            "WHERE u.username = :username")
    List<Ticket> findAllByUser(String username);

    /**
     * Finds all tickets within a specific board, verifying the user's project membership.
     * @param boardId the ID of the board
     * @param username the username of the member
     * @return a list of tickets in the board that the user is allowed to see
     */
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.board b " +
            "JOIN b.project p " +
            "JOIN p.users u " +
            "WHERE b.id = :boardId AND u.username = :username")
    List<Ticket> findAllByBoardAndUser(Long boardId, String username);

    /**
     * Finds tickets that contain the given text in their title, ignoring case.
     * @param title the search keyword
     * @return a list of tickets matching the keyword
     */
    List<Ticket> findByTitleContainingIgnoreCase(String title);

    /**
     * Secure search that finds tickets by title only in projects the user actually belongs to.
     * @param query the text to look for in titles
     * @param username the user performing the search
     * @return a list of tickets the user is allowed to see
     */
    @Query("SELECT DISTINCT t FROM Ticket t " +
            "JOIN t.board b " +
            "JOIN b.project p " +
            "JOIN p.users u " +
            "WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "AND u.username = :username")
    List<Ticket> searchByTitleAndUser(String query, String username);

}

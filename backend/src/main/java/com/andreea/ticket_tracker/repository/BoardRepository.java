package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByProjectId(Long projectId);

    @Query("SELECT b FROM Board b JOIN b.project p JOIN p.users u WHERE u.username = :username")
    List<Board> findAllByUser(String username);

    @Query("SELECT b FROM Board b JOIN b.project p JOIN p.users u " +
            "WHERE p.id = :projectId AND u.username = :username")
    List<Board> findAllByProjectAndUser(Long projectId, String username);
}

package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByProjectId(Long projectId);
    List<Board> findAllByProject_Users_Username(String username);
    List<Board> findAllByProjectIdAndProject_Users_Username(Long projectId, String username);
}

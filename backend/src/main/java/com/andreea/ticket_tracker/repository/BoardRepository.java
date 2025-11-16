package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}

package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByBoardId(Long boardId);
    List<Ticket> findAllByBoard_Project_Users_Username(String username);
    List<Ticket> findAllByBoardIdAndBoard_Project_Users_Username(Long boardId, String username);
}

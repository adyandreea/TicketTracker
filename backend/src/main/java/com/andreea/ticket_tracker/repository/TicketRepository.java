package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByBoardId(Long boardId);
}

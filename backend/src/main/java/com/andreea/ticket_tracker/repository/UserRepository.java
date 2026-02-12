package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}

package com.andreea.ticket_tracker.repository;

import com.andreea.ticket_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Set<User> findAllByProjects_Id(Long projectId);
}

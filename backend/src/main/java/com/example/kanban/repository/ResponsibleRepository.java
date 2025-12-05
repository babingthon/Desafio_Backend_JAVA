package com.example.kanban.repository;

import com.example.kanban.domain.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long> {
    Optional<Responsible> findByEmail(String email);
}
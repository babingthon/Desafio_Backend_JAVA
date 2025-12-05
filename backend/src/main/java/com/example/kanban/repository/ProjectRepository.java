package com.example.kanban.repository;

import com.example.kanban.domain.Project;
import com.example.kanban.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByStatus(ProjectStatus status, Pageable pageable);
}
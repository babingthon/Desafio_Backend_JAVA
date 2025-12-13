package com.example.kanban.repository;

import com.example.kanban.domain.Project;
import com.example.kanban.domain.Responsible; // 💥 NOVO IMPORT
import com.example.kanban.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByStatus(ProjectStatus status, Pageable pageable);

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatus();

    Page<Project> findAllByResponsiblesContains(Responsible responsible, Pageable pageable);

    Page<Project> findAllByStatusAndResponsiblesContains(ProjectStatus status, Responsible responsible, Pageable pageable);

    List<Project> findAllByResponsiblesContains(Responsible responsible);
}
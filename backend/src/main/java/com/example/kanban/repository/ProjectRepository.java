package com.example.kanban.repository;

import com.example.kanban.domain.Project;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByStatus(ProjectStatus status, Pageable pageable);

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatus();

    Page<Project> findAllByResponsiblesContains(Responsible responsible, Pageable pageable);

    Page<Project> findAllByStatusAndResponsiblesContains(ProjectStatus status, Responsible responsible, Pageable pageable);

    List<Project> findAllByResponsiblesContains(Responsible responsible);

    @Query("SELECT p.status, COUNT(p) FROM Project p WHERE :responsible MEMBER OF p.responsibles GROUP BY p.status")
    List<Object[]> countProjectsByStatusByResponsible(Responsible responsible);

    @Query(value = "SELECT p.status, AVG(CURRENT_DATE - p.scheduled_end_date) " +
            "FROM kanban.project p " +
            "WHERE p.status IN ('IN_PROGRESS', 'DELAYED') " +
            "AND p.scheduled_end_date < CURRENT_DATE " +
            "GROUP BY p.status", nativeQuery = true)
    List<Object[]> calculateAverageDelayDays();

    @Query(value = "SELECT p.status, AVG(CURRENT_DATE - p.scheduled_end_date) " +
            "FROM kanban.project p JOIN kanban.project_responsible pr ON p.id = pr.project_id " +
            "WHERE p.status IN ('IN_PROGRESS', 'DELAYED') " +
            "AND pr.responsible_id = :responsibleId " +
            "AND p.scheduled_end_date < CURRENT_DATE " +
            "GROUP BY p.status", nativeQuery = true)
    List<Object[]> calculateAverageDelayDaysByResponsibleId(@Param("responsibleId") Long responsibleId);
}

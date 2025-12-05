package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.StatusTransitionRequest;
import com.example.kanban.calculator.ProjectMetricsCalculator;
import com.example.kanban.exception.ResourceNotFoundException;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.repository.ProjectRepository;
import com.example.kanban.repository.ResponsibleRepository;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.enums.ProjectStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService implements IProjectService {

    private final ProjectRepository projectRepository;
    private final ResponsibleRepository responsibleRepository;
    private final ProjectMapper projectMapper;

    @Override
    public Project create(ProjectRequest request) {
        Project project = projectMapper.toEntity(request);

        Set<Responsible> responsibles = loadResponsibles(request.responsibleIds());
        project.setResponsibles(responsibles);

        recalculateStatusAndMetrics(project);

        return projectRepository.save(project);
    }

    @Override
    public Project update(Long id, ProjectRequest request) {
        Project project = findById(id);

        projectMapper.updateEntityFromRequest(project, request);

        Set<Responsible> responsibles = loadResponsibles(request.responsibleIds());
        project.setResponsibles(responsibles);

        recalculateStatusAndMetrics(project);

        return projectRepository.save(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAllByStatus(ProjectStatus status, Pageable pageable) {
        return projectRepository.findAllByStatus(status, pageable);
    }


    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public Project transitionStatus(Long id, StatusTransitionRequest request) {
        Project project = findById(id);
        ProjectStatus currentStatus = project.getStatus();
        ProjectStatus newStatus = request.newStatus();
        LocalDate today = LocalDate.now();

        if (currentStatus == newStatus) {
            return project;
        }

        applyTransitionLogic(project, currentStatus, newStatus, today);

        ProjectStatus calculatedStatus = ProjectMetricsCalculator.calculateStatus(project, today);

        if (calculatedStatus != newStatus) {
            throw new IllegalArgumentException(
                    "Transition blocked. New status must be '" + calculatedStatus +
                            "' based on current dates. Adjust project dates to match the desired status."
            );
        }

        project.setStatus(newStatus);
        return projectRepository.save(project);
    }

    private void applyTransitionLogic(Project project, ProjectStatus current, ProjectStatus next, LocalDate today) {

        switch (current) {
            case TO_START:
                if (next == ProjectStatus.IN_PROGRESS) {
                    project.setActualStartDate(today);
                } else if (next == ProjectStatus.COMPLETED) {
                    project.setActualEndDate(today);
                } else if (next == ProjectStatus.DELAYED) {
                    if (project.getScheduledStartDate() != null && today.isBefore(project.getScheduledStartDate())) {
                        throw new IllegalArgumentException("Cannot transition to DELAYED before scheduled start date.");
                    }
                }
                break;
            case IN_PROGRESS:
                if (next == ProjectStatus.COMPLETED) {
                    project.setActualEndDate(today);
                } else if (next == ProjectStatus.TO_START) {
                    project.setActualStartDate(null);
                } else if (next == ProjectStatus.DELAYED) {
                    throw new IllegalArgumentException("Transition to DELAYED is automatically calculated by overdue dates.");
                }
                break;
            case DELAYED:
                if (next == ProjectStatus.COMPLETED) {
                    project.setActualEndDate(today);
                } else if (next == ProjectStatus.TO_START || next == ProjectStatus.IN_PROGRESS) {
                    throw new IllegalArgumentException("Transition blocked. Scheduled dates must be corrected to exit DELAYED status.");
                }
                break;
            case COMPLETED:
                if (next == ProjectStatus.TO_START || next == ProjectStatus.IN_PROGRESS || next == ProjectStatus.DELAYED) {
                    project.setActualEndDate(null);
                    ProjectStatus statusAfterRemoval = ProjectMetricsCalculator.calculateStatus(project, today);
                    if (statusAfterRemoval != next) {
                        throw new IllegalArgumentException(
                                "Transition blocked. Removing Actual End Date results in status '" + statusAfterRemoval +
                                        "'. Please adjust scheduled dates to match the desired status '" + next + "'."
                        );
                    }
                }
                break;
        }
    }

    private void recalculateStatusAndMetrics(Project project) {
        project.setStatus(ProjectMetricsCalculator.calculateStatus(project, LocalDate.now()));
    }

    private Set<Responsible> loadResponsibles(Set<Long> responsibleIds) {
        if (responsibleIds == null || responsibleIds.isEmpty()) {
            return Set.of();
        }

        Set<Responsible> responsibles = responsibleRepository.findAllById(responsibleIds).stream().collect(Collectors.toSet());

        if (responsibles.size() != responsibleIds.size()) {
            throw new ResourceNotFoundException("One or more Responsible IDs were not found.");
        }
        return responsibles;
    }
}
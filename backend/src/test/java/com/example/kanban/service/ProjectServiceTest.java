package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.StatusTransitionRequest;
import com.example.kanban.calculator.ProjectMetricsCalculator;
import com.example.kanban.domain.User;
import com.example.kanban.domain.enums.Role;
import com.example.kanban.exception.ResourceNotFoundException;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.enums.ProjectStatus;
import com.example.kanban.repository.ProjectRepository;
import com.example.kanban.repository.ResponsibleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ResponsibleRepository responsibleRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private ProjectRequest validRequest;
    private Project project;
    private Responsible responsible;

    @BeforeEach
    void setUp() {
        responsible = new Responsible();
        responsible.setId(1L);
        responsible.setName("Test Responsible");
        responsible.setEmail("resp@test.com");
        responsible.setJobTitle("Job");

        validRequest = new ProjectRequest(
                "Test Project",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1),
                Set.of(1L),
                null,
                null
        );

        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setResponsibles(Set.of(responsible));
        project.setStatus(ProjectStatus.TO_START);
    }

    @Test
    void update_ShouldThrowException_WhenResponsibleNotFound() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(responsibleRepository.findAllById(Set.of(99L))).thenReturn(List.of());

        ProjectRequest invalidRequest = new ProjectRequest(
                "Update Name", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1),
                Set.of(99L), null, null
        );

        assertThrows(ResourceNotFoundException.class, () -> projectService.update(1L, invalidRequest));
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(projectRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> projectService.delete(99L));
        verify(projectRepository, never()).deleteById(anyLong());
    }

    @Test
    void transitionStatus_ShouldUpdateActualStartDate_WhenToStartToInProgress() {
        try (MockedStatic<ProjectMetricsCalculator> mockedCalculator = mockStatic(ProjectMetricsCalculator.class)) {
            project.setStatus(ProjectStatus.TO_START);
            StatusTransitionRequest request = new StatusTransitionRequest(ProjectStatus.IN_PROGRESS);

            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

            mockedCalculator.when(() -> ProjectMetricsCalculator.calculateStatus(any(Project.class), any(LocalDate.class)))
                    .thenReturn(ProjectStatus.IN_PROGRESS);

            when(projectRepository.save(any(Project.class))).thenReturn(project);

            Project updated = projectService.transitionStatus(1L, request);

            assertNotNull(updated);
            assertEquals(ProjectStatus.IN_PROGRESS, updated.getStatus());
            assertEquals(LocalDate.now(), updated.getActualStartDate());
        }
    }

    @Test
    void transitionStatus_ShouldThrowException_WhenCalculatedStatusDoesNotMatchRequest() {
        try (MockedStatic<ProjectMetricsCalculator> mockedCalculator = mockStatic(ProjectMetricsCalculator.class)) {
               project.setStatus(ProjectStatus.TO_START);
            StatusTransitionRequest request = new StatusTransitionRequest(ProjectStatus.COMPLETED);

            when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

            mockedCalculator.when(() -> ProjectMetricsCalculator.calculateStatus(any(Project.class), any(LocalDate.class)))
                    .thenReturn(ProjectStatus.DELAYED);

            assertThrows(IllegalArgumentException.class, () -> projectService.transitionStatus(1L, request));
            verify(projectRepository, never()).save(any(Project.class));
        }
    }

    @Test
    void transitionStatus_ShouldThrowException_WhenAttemptingManualTransitionToDelayed() {
        project.setStatus(ProjectStatus.IN_PROGRESS);
        StatusTransitionRequest request = new StatusTransitionRequest(ProjectStatus.DELAYED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertThrows(IllegalArgumentException.class, () -> projectService.transitionStatus(1L, request));
    }

}
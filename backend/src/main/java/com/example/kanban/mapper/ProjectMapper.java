package com.example.kanban.mapper;

import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.ProjectResponse;
import com.example.kanban.api.dto.ResponsibleResponse;
import com.example.kanban.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ResponsibleMapper responsibleMapper;

    public Project toEntity(ProjectRequest request) {
        Project entity = new Project();
        entity.setName(request.name());
        entity.setScheduledStartDate(request.scheduledStartDate());
        entity.setScheduledEndDate(request.scheduledEndDate());
        entity.setActualStartDate(request.actualStartDate());
        entity.setActualEndDate(request.actualEndDate());

        return entity;
    }

    public void updateEntityFromRequest(Project entity, ProjectRequest request) {
        entity.setName(request.name());
        entity.setScheduledStartDate(request.scheduledStartDate());
        entity.setScheduledEndDate(request.scheduledEndDate());
        entity.setActualStartDate(request.actualStartDate());
        entity.setActualEndDate(request.actualEndDate());
    }

    public ProjectResponse toResponse(Project entity) {
        Set<ResponsibleResponse> responsibleResponses = entity.getResponsibles().stream()
                .map(responsibleMapper::toResponse)
                .collect(Collectors.toSet());

        return new ProjectResponse(
                entity.getId(),
                entity.getName(),
                entity.getStatus(),
                entity.getScheduledStartDate(),
                entity.getScheduledEndDate(),
                entity.getActualStartDate(),
                entity.getActualEndDate(),
                entity.getDaysOfDelay(),
                entity.getRemainingTimePercentage(),
                responsibleResponses,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
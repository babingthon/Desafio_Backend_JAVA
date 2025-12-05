package com.example.kanban.api.dto;

import com.example.kanban.domain.enums.ProjectStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record ProjectResponse(
        Long id,
        String name,
        ProjectStatus status,
        LocalDate scheduledStartDate,
        LocalDate scheduledEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,

        Long daysOfDelay,
        Double remainingTimePercentage,

        Set<ResponsibleResponse> responsibles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
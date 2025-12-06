package com.example.kanban.api.dto;

import com.example.kanban.domain.enums.ProjectStatus;

public record ProjectIndicatorResponse(
        ProjectStatus status,
        Double value,
        String metricType
) {}
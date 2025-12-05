package com.example.kanban.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        String name,

        @NotNull(message = "Scheduled start date is required")
        LocalDate scheduledStartDate,

        @NotNull(message = "Scheduled end date is required")
        LocalDate scheduledEndDate,

        Set<Long> responsibleIds,

        LocalDate actualStartDate,
        LocalDate actualEndDate
) {}

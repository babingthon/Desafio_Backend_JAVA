package com.example.kanban.api.dto;

import com.example.kanban.domain.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public record StatusTransitionRequest(
        @NotNull(message = "New status is required.")
        ProjectStatus newStatus
) {}

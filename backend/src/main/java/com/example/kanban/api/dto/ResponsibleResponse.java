package com.example.kanban.api.dto;

import java.time.LocalDateTime;

public record ResponsibleResponse(
        Long id,
        String name,
        String email,
        String jobTitle,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
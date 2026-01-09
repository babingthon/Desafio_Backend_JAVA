package com.example.kanban.api.dto.user;

public record UserProfileResponse(
        String name,
        String jobTitle,
        String email
) {}

package com.example.kanban.api.dto.auth;

public record TokenResponse(
        String token,
        String email
) {}

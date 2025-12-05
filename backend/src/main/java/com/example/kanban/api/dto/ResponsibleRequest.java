package com.example.kanban.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ResponsibleRequest(
        @NotBlank @Length(max = 200)
        String name,

        @NotBlank @Email @Length(max = 200)
        String email,

        @Length(max = 100)
        String jobTitle
) {}
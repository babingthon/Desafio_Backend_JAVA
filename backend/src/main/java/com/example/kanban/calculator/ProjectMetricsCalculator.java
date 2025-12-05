package com.example.kanban.calculator;

import com.example.kanban.domain.Project;
import com.example.kanban.domain.enums.ProjectStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class ProjectMetricsCalculator {

    private ProjectMetricsCalculator() {

    }

    public static ProjectStatus calculateStatus(Project project, LocalDate today) {
        if (project.getActualEndDate() != null) {
            return ProjectStatus.COMPLETED;
        }

        boolean overdueStart = project.getScheduledStartDate() != null &&
                project.getScheduledStartDate().isBefore(today) &&
                project.getActualStartDate() == null;

        boolean overdueEnd = project.getScheduledEndDate() != null &&
                project.getScheduledEndDate().isBefore(today) &&
                project.getActualEndDate() == null;

        if (overdueStart || overdueEnd) {
            return ProjectStatus.DELAYED;
        }

        if (project.getActualStartDate() != null &&
                project.getScheduledEndDate() != null &&
                project.getScheduledEndDate().isAfter(today) &&
                project.getActualEndDate() == null) {

            return ProjectStatus.IN_PROGRESS;
        }

        return ProjectStatus.TO_START;
    }

    public static Long calculateDaysOfDelay(Project project, LocalDate today) {
        if (project.getStatus() == ProjectStatus.COMPLETED || project.getStatus() == ProjectStatus.TO_START) {
            return 0L;
        }

        if (project.getScheduledEndDate() != null &&
                project.getScheduledEndDate().isBefore(today) &&
                project.getActualEndDate() == null) {

            return ChronoUnit.DAYS.between(project.getScheduledEndDate(), today);
        }

        return 0L;
    }

    public static Double calculateRemainingTimePercentage(Project project, LocalDate today) {
        if (project.getStatus() == ProjectStatus.COMPLETED || project.getStatus() == ProjectStatus.TO_START ||
                (project.getScheduledEndDate() != null && today.isAfter(project.getScheduledEndDate()))) {

            return 0.0;
        }

        if (project.getScheduledStartDate() == null || project.getScheduledEndDate() == null) {
            return 0.0;
        }

        long totalDays = ChronoUnit.DAYS.between(project.getScheduledStartDate(), project.getScheduledEndDate());

        if (totalDays <= 0) {
            return 0.0;
        }

        long usedDays = ChronoUnit.DAYS.between(project.getScheduledStartDate(), today);
        long remainingDays = totalDays - usedDays;

        if (remainingDays < 0) {
            return 0.0;
        }

        return ((double) remainingDays / totalDays) * 100.0;
    }
}
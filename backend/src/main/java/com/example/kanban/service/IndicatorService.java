package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectIndicatorResponse;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.enums.ProjectStatus;
import com.example.kanban.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndicatorService implements IIndicatorService {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Override
    public List<ProjectIndicatorResponse> getProjectCountByStatus() {
        List<Object[]> results = projectRepository.countProjectsByStatus();

        return results.stream()
                .map(row -> new ProjectIndicatorResponse(
                        (ProjectStatus) row[0],
                        ((Long) row[1]).doubleValue(),
                        "PROJECT_COUNT"
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectIndicatorResponse> getAverageDelayDaysByStatus() {

        Map<ProjectStatus, Double> averageByStatus = projectRepository.findAll().stream()
                .filter(project -> project.getDaysOfDelay() != null && project.getDaysOfDelay() > 0)
                .collect(Collectors.groupingBy(
                        Project::getStatus,
                        Collectors.averagingLong(Project::getDaysOfDelay)
                ));

        List<ProjectIndicatorResponse> avgDelay = averageByStatus.entrySet().stream()
                .map(entry -> new ProjectIndicatorResponse(
                        entry.getKey(),
                        entry.getValue(),
                        "AVERAGE_DELAY_DAYS"
                ))
                .collect(Collectors.toList());

        return avgDelay;
    }
}
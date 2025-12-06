package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectIndicatorResponse;
import java.util.List;

public interface IIndicatorService {
    List<ProjectIndicatorResponse> getProjectCountByStatus();
    List<ProjectIndicatorResponse> getAverageDelayDaysByStatus();
}

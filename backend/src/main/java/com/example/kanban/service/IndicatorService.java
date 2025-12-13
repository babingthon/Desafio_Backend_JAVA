package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectIndicatorResponse;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.User;
import com.example.kanban.domain.enums.ProjectStatus;
import com.example.kanban.domain.enums.Role;
import com.example.kanban.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndicatorService implements IIndicatorService {

    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectIndicatorResponse> getProjectCountByStatus(User user) {
        return List.of();
    }

    @Override
    public List<ProjectIndicatorResponse> getAverageDelayDaysByStatus(User user) {
        return List.of();
    }

    public List<Object[]> countByStatusAndUserContext(User user) {
        if (user.getRole() == Role.ADMIN) {
            return projectRepository.countProjectsByStatus();
        } else {
            Responsible responsible = user.getResponsible();
            if (responsible == null) {
                return List.of();
            }

            return projectRepository.countProjectsByStatusByResponsible(responsible);
        }
    }

    public List<Object[]> calculateAverageDelayDaysAndUserContext(User user) {
        if (user.getRole() == Role.ADMIN) {
            return projectRepository.calculateAverageDelayDays();
        } else {
            Responsible responsible = user.getResponsible();
            if (responsible == null) {
                return List.of();
            }

            return projectRepository.calculateAverageDelayDaysByResponsibleId(responsible.getId());
        }
    }
}
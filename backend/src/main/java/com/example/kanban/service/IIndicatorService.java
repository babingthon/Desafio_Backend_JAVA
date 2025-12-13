package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectIndicatorResponse;
import com.example.kanban.domain.User; // 💥 NOVO IMPORT 💥
import java.util.List;

public interface IIndicatorService {

    List<Object[]> countByStatusAndUserContext(User user);

    List<Object[]> calculateAverageDelayDaysAndUserContext(User user);
}
package com.example.kanban.service;

import com.example.kanban.domain.User;
import com.example.kanban.domain.Responsible;
import com.example.kanban.domain.enums.Role;
import com.example.kanban.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IndicatorServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private IndicatorService indicatorService;

    private User adminUser;
    private User regularUser;
    private Responsible responsibleAlice;
    private List<Object[]> mockAdminResult;
    private List<Object[]> mockUserResult;

    @BeforeEach
    void setUp() {
        responsibleAlice = Responsible.builder().id(2L).name("Alice").build();

        adminUser = User.builder().role(Role.ADMIN).build();

        regularUser = User.builder()
                .email("alice")
                .role(Role.USER)
                .responsible(responsibleAlice)
                .build();

        if (regularUser.getResponsible() == null) {
            throw new RuntimeException("Responsible is NULL on mock user. Check Lombok setup.");
        }

        mockAdminResult = new ArrayList<>();
        mockAdminResult.add(new Object[]{"DONE", 5L});
        mockAdminResult.add(new Object[]{"IN_PROGRESS", 10L});

        mockUserResult = new ArrayList<>();
        mockUserResult.add(new Object[]{"IN_PROGRESS", 1L});
    }

    @Test
    void shouldReturnAllProjectsCount_whenUserIsAdmin() {
        when(projectRepository.countProjectsByStatus()).thenReturn(mockAdminResult);

        List<Object[]> result = indicatorService.countByStatusAndUserContext(adminUser);

        assertEquals(2, result.size());

        verify(projectRepository, times(1)).countProjectsByStatus();

        verify(projectRepository, never()).countProjectsByStatusByResponsibleId(anyLong());
    }

    @Test
    void shouldReturnEmptyListForCount_whenRegularUserHasNoResponsible() {
        User userWithoutResponsible = User.builder().role(Role.USER).build();

        List<Object[]> result = indicatorService.countByStatusAndUserContext(userWithoutResponsible);

        assertEquals(Collections.emptyList(), result);

        verify(projectRepository, never()).countProjectsByStatus();

        verify(projectRepository, never()).countProjectsByStatusByResponsibleId(anyLong());
    }

    @Test
    void shouldReturnAllProjectsDelayAverage_whenUserIsAdmin() {
        when(projectRepository.calculateAverageDelayDays()).thenReturn(mockAdminResult);

        List<Object[]> result = indicatorService.calculateAverageDelayDaysAndUserContext(adminUser);

        assertEquals(2, result.size());

        verify(projectRepository, times(1)).calculateAverageDelayDays();

        verify(projectRepository, never()).calculateAverageDelayDaysByResponsibleId(anyLong());
    }

    @Test
    void shouldReturnOnlyUserProjectsDelayAverage_whenUserIsRegularUser() {
        when(projectRepository.calculateAverageDelayDaysByResponsibleId(2L)).thenReturn(mockUserResult);

        List<Object[]> result = indicatorService.calculateAverageDelayDaysAndUserContext(regularUser);

        assertEquals(1, result.size());

        verify(projectRepository, times(1)).calculateAverageDelayDaysByResponsibleId(2L);

        verify(projectRepository, never()).calculateAverageDelayDays();
    }
}
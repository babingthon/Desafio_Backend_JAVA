package com.example.kanban.security.service;

import com.example.kanban.domain.User;
import com.example.kanban.domain.Responsible;
import com.example.kanban.service.IUserService;
import com.example.kanban.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final IUserService userService;
    private final ProjectRepository projectRepository;

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public boolean isProjectResponsible(Long projectId) {

        User user = userService.findUserByEmail(getAuthenticatedUserEmail());

        if (user.getRole().name().equals("ADMIN")) {
            return true;
        }

        Responsible authenticatedResponsible = user.getResponsible();

        if (authenticatedResponsible == null) {
            return false;
        }

        return projectRepository.findById(projectId)
                .map(project -> project.getResponsibles().contains(authenticatedResponsible))
                .orElse(false);
    }

    public boolean isOwnProfile(Long responsibleId) {

        User user = userService.findUserByEmail(getAuthenticatedUserEmail());

        if (user.getRole().name().equals("ADMIN")) {
            return true;
        }

        Responsible authenticatedResponsible = user.getResponsible();

        if (authenticatedResponsible == null) {
            return false;
        }

        return authenticatedResponsible.getId().equals(responsibleId);
    }
}
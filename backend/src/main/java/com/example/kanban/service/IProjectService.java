package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.StatusTransitionRequest;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.User;
import com.example.kanban.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProjectService {

    Project create(ProjectRequest request, User user);

    Project update(Long id, ProjectRequest request);

    Project findById(Long id);

    void delete(Long id);

    Project transitionStatus(Long id, StatusTransitionRequest request);

    Page<Project> findAllByUserContext(User user, Pageable pageable);

    Page<Project> findAllByStatusAndUserContext(ProjectStatus status, User user, Pageable pageable);

    List<Project> findAllProjects(User user);
}
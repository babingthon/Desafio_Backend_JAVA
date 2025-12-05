package com.example.kanban.service;

import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.StatusTransitionRequest;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProjectService {
    Project create(ProjectRequest request);
    Project update(Long id, ProjectRequest request);
    Project findById(Long id);
    Page<Project> findAll(Pageable pageable);
    void delete(Long id);

    Project transitionStatus(Long id, StatusTransitionRequest request);

    Page<Project> findAllByStatus(ProjectStatus status, Pageable pageable);
}
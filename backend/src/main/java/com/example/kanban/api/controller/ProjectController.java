package com.example.kanban.api.controller;

import com.example.kanban.annotations.ApiBearerAuth;
import com.example.kanban.api.dto.ProjectRequest;
import com.example.kanban.api.dto.ProjectResponse;
import com.example.kanban.api.dto.StatusTransitionRequest;
import com.example.kanban.service.IProjectService;
import com.example.kanban.mapper.ProjectMapper;
import com.example.kanban.domain.Project;
import com.example.kanban.domain.enums.ProjectStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "CRUD operations and status management for Kanban Projects")
public class ProjectController {

    private final IProjectService projectService;
    private final ProjectMapper projectMapper;

    @ApiBearerAuth
    @Operation(summary = "Get all Projects with pagination", description = "Retrieves a paginated list of all projects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of paginated list.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Project> projectPage = projectService.findAll(pageable);
        Page<ProjectResponse> responsePage = projectPage.map(projectMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @ApiBearerAuth
    @Operation(summary = "Get Projects filtered by status", description = "Retrieves a paginated list of projects filtered by a specific Kanban status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of paginated list.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class)))
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByStatus(
            @PathVariable ProjectStatus status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "name")
            @RequestParam(defaultValue = "name") String sort,
            @Parameter(description = "Sort direction", example = "asc", schema = @Schema(allowableValues = {"asc", "desc"}))
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Project> projectPage = projectService.findAllByStatus(status, pageable);

        Page<ProjectResponse> responsePage = projectPage.map(projectMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @ApiBearerAuth
    @Operation(summary = "Transition Project Status", description = "Changes the project status according to the Kanban transition rules, applying automatic date updates and validation blocks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status transitioned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transition or date conflict (message provides adjustment advice).", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Project not found.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority(RoleConstants.USER)")
    public ResponseEntity<ProjectResponse> transitionStatus(@PathVariable Long id, @Valid @RequestBody StatusTransitionRequest request) {
        Project project = projectService.transitionStatus(id, request);
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @ApiBearerAuth
    @Operation(summary = "Create a new Project", description = "Creates a project, associates responsibles, and recalculates its initial status and metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or data validation failure.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "One or more Responsible IDs were not found.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    @PreAuthorize("hasAuthority(RoleConstants.USER)")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
        Project project = projectService.create(request);
        return new ResponseEntity<>(projectMapper.toResponse(project), HttpStatus.CREATED);
    }

    @Operation(summary = "Get Project by ID", description = "Retrieves a single project by its ID, including calculated metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "404", description = "Project not found.", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(RoleConstants.USER)")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        Project project = projectService.findById(id);
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @ApiBearerAuth
    @Operation(summary = "Update an existing Project", description = "Updates project details, responsible associations, and recalculates status/metrics.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or data validation failure.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Project or Responsible not found.", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(RoleConstants.USER)")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        Project project = projectService.update(id, request);
        return ResponseEntity.ok(projectMapper.toResponse(project));
    }

    @ApiBearerAuth
    @Operation(summary = "Delete a Project", description = "Deletes a project by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Project not found.", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(RoleConstants.ADMIN)")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
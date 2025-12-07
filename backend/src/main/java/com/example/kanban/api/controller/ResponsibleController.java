package com.example.kanban.api.controller;

import com.example.kanban.annotations.ApiBearerAuth;
import com.example.kanban.api.dto.ResponsibleRequest;
import com.example.kanban.api.dto.ResponsibleResponse;
import com.example.kanban.service.IResponsibleService;
import com.example.kanban.mapper.ResponsibleMapper;
import com.example.kanban.domain.Responsible;

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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responsible")
@Tag(name = "Responsible", description = "CRUD operations for Project Responsible")
public class ResponsibleController {

    private final IResponsibleService responsibleService;
    private final ResponsibleMapper responsibleMapper;

    @ApiBearerAuth
    @Operation(summary = "Create a new Responsible", description = "Persists a new project responsible and ensures email uniqueness.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Responsible created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsibleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or data validation failure (e.g., email already in use).", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<ResponsibleResponse> createResponsible(@Valid @RequestBody ResponsibleRequest request) {
        Responsible responsible = responsibleService.create(request);
        return new ResponseEntity<>(responsibleMapper.toResponse(responsible), HttpStatus.CREATED);
    }

    @ApiBearerAuth
    @Operation(summary = "Get all Responsibles with pagination", description = "Retrieves a paginated list of all project responsibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of paginated list.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsibleResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ResponsibleResponse>> getAllResponsibles(
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

        Page<Responsible> responsiblePage = responsibleService.findAll(pageable);
        Page<ResponsibleResponse> responsePage = responsiblePage.map(responsibleMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @ApiBearerAuth
    @Operation(summary = "Get Responsible by ID", description = "Retrieves a single project responsible by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsible found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsibleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Responsible not found.", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponsibleResponse> getResponsibleById(@PathVariable Long id) {
        Responsible responsible = responsibleService.findById(id);
        return ResponseEntity.ok(responsibleMapper.toResponse(responsible));
    }

    @ApiBearerAuth
    @Operation(summary = "Update an existing Responsible", description = "Updates details of a project responsible by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsible updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsibleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or email conflict.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Responsible not found.", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponsibleResponse> updateResponsible(@PathVariable Long id, @Valid @RequestBody ResponsibleRequest request) {
        Responsible responsible = responsibleService.update(id, request);
        return ResponseEntity.ok(responsibleMapper.toResponse(responsible));
    }

    @ApiBearerAuth
    @Operation(summary = "Delete a Responsible", description = "Deletes a project responsible by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Responsible deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Responsible not found.", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponsible(@PathVariable Long id) {
        responsibleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
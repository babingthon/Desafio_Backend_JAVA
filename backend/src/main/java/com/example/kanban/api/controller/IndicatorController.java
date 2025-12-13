package com.example.kanban.api.controller;

import com.example.kanban.annotations.ApiBearerAuth;
import com.example.kanban.annotations.IsUser;
import com.example.kanban.api.dto.ProjectIndicatorResponse;
import com.example.kanban.domain.User;
import com.example.kanban.service.IIndicatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/indicators")
@Tag(name = "Indicators", description = "Performance metrics and aggregated data for Kanban Projects")
public class IndicatorController {

    private final IIndicatorService indicatorService;

    @ApiBearerAuth
    @Operation(summary = "Get project count grouped by status", description = "Calculates the number of projects per status, filtered by projects the authenticated user is responsible for.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of project counts.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectIndicatorResponse.class)))
    })
    @GetMapping("/count-by-status")
    @IsUser
    public ResponseEntity<List<ProjectIndicatorResponse>> getProjectCountByStatus(
            @AuthenticationPrincipal User user) {

        List<ProjectIndicatorResponse> indicators = indicatorService.getProjectCountByStatus(user);
        return ResponseEntity.ok(indicators);
    }

    @ApiBearerAuth
    @Operation(summary = "Get average delay days grouped by status", description = "Calculates the average delay days for projects by status, filtered by projects the authenticated user is responsible for.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of average delay days.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectIndicatorResponse.class)))
    })
    @GetMapping("/average-delay")
    @IsUser
    public ResponseEntity<List<ProjectIndicatorResponse>> getAverageDelayDaysByStatus(
            @AuthenticationPrincipal User user) { 

        List<ProjectIndicatorResponse> indicators = indicatorService.getAverageDelayDaysByStatus(user);
        return ResponseEntity.ok(indicators);
    }
}
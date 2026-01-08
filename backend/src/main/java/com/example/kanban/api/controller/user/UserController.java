package com.example.kanban.api.controller.user;

import com.example.kanban.annotations.ApiBearerAuth;
import com.example.kanban.api.dto.user.PasswordChangeRequest;
import com.example.kanban.api.dto.user.RegistrationRequest;
import com.example.kanban.api.dto.user.UserProfileResponse;
import com.example.kanban.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Registration", description = "Endpoints for creating new user accounts and responsible profiles")
public class UserController {

    private final IUserService userService;

    @ApiBearerAuth
    @Operation(summary = "Update current user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Current password incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody @Valid PasswordChangeRequest request,
            Authentication authentication) {

        userService.updatePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @ApiBearerAuth
    @Operation(summary = "Get current logged user profile data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile data retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing token",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Responsible profile not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        UserProfileResponse profile = userService.getLoggedUserProfile(authentication.getName());
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Register a new user and create the linked Responsible profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation failed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = com.example.kanban.exception.ErrorDetails.class))),
            @ApiResponse(responseCode = "409", description = "Conflict: User with this email already exists",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationRequest request) {

        userService.registerNewUser(request);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}

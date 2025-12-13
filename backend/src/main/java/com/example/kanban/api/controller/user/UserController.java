package com.example.kanban.api.controller.user;

import com.example.kanban.api.dto.user.RegistrationRequest;
import com.example.kanban.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

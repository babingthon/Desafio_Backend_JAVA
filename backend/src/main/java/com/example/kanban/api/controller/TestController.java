package com.example.kanban.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "Test Controller", description = "Test Endpoints")
public class TestController {

    @GetMapping("/ping")
    @Operation(summary = "Connection Test")
    public String ping() {
        return "PONG - " + System.currentTimeMillis();
    }

    @GetMapping("/swagger-check")
    @Operation(summary = "Checks if Swagger is operational")
    public String swaggerCheck() {
        return "Swagger should be available at /swagger-ui.html";
    }
}
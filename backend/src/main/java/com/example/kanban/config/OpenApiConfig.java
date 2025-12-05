package com.example.kanban.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.utils.SpringDocUtils;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI kanbanOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kanban API")
                        .description("Complete API for Kanban project management")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Kanban Team")
                                .email("support@kanban.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local server")
                ));
    }
}
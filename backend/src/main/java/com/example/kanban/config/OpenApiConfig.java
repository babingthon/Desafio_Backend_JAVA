package com.example.kanban.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String API_V1_PATH = "/api/v1";

    @Bean
    public OpenAPI unifiedOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(API_V1_PATH)
                        .description("Servidor Base da API v1")
                )
                .info(new Info()
                        .title("Kanban Backend API")
                        .version("1.0.0")
                        .description("API para gerenciamento de projetos Kanban.")
                );
    }
}

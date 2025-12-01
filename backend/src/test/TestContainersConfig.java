package com.example.kanban.test;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Testcontainers
public abstract class TestContainersConfig {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15-alpine");

    @Container
    public static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName("kanban")
                    .withUsername("kanban")
                    .withPassword("kanban");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }
}
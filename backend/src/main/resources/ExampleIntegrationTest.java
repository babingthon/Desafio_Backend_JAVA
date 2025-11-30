package com.example.kanban.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test-postgres")
public class ExampleIntegrationTest extends TestContainersConfig {

    @Test
    void contextLoads() {
    }
}

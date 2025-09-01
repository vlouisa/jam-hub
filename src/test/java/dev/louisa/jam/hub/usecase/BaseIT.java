package dev.louisa.jam.hub.usecase;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@ActiveProfiles("develop")
@Slf4j
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16.4-alpine:///databasename",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
//    "spring.datasource.url=jdbc:postgresql://localhost:54321/jam_hub_db?user=jam_hub_owner&password=jam_hub",
})
@Tag("integration-test")
@Transactional
public class BaseIT {
    
}

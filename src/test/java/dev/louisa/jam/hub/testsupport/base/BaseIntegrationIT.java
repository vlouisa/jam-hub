package dev.louisa.jam.hub.testsupport.base;

import dev.louisa.jam.hub.infrastructure.security.jwt.config.JwtTestConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Tag("integration-test")
@Tag("domain-layer")
@Transactional
@SpringBootTest
@ActiveProfiles("repository-it")
@Import(JwtTestConfig.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16.4-alpine:///databasename",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "jwt-key-resolver.active-bundle=2025.09.17.170803"
})
// Disable security -> security should be tested in web/interfaces layer
@AutoConfigureMockMvc(addFilters = false)
public class BaseIntegrationIT {}

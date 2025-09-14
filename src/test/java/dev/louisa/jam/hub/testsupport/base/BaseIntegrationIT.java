package dev.louisa.jam.hub.testsupport.base;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Tag("integration-test")
@Tag("domain-layer")
@Transactional
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16.4-alpine:///databasename",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
// Disable security -> security should be tested in web/interfaces layer
@AutoConfigureMockMvc(addFilters = false)
public class BaseIntegrationIT {}

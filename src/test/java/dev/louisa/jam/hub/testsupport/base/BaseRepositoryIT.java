package dev.louisa.jam.hub.testsupport.base;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@Tag("integration-test")
@Tag("domain-layer")
@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16.4-alpine:///databasename",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
})
public class BaseRepositoryIT {}

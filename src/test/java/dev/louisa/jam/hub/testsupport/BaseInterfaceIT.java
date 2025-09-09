package dev.louisa.jam.hub.testsupport;

import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.infrastructure.ErrorResponse;
import dev.louisa.victor.mock.rest.MockRest;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Tag("integration-test")
@Tag("interface-layer")
@Transactional
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16.4-alpine:///databasename",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
@AutoConfigureMockMvc
public class BaseInterfaceIT {
    
    @Autowired
    protected MockRest api;
    protected ErrorResponse errorResponse(JamHubError error) {
        return errorResponse(error, List.of());
    }

    protected ErrorResponse errorResponse(JamHubError error, List<String> context) {
        return ErrorResponse.builder()
                .errorCode(error.getDomainCode() + "-" + error.getErrorCode())
                .context(context)
                .message(error.getMessage())
                .build();
    }
}

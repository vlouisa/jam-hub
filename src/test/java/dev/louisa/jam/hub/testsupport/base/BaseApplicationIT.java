package dev.louisa.jam.hub.testsupport.base;

import dev.louisa.jam.hub.infrastructure.security.jwt.config.JwtTestConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Tag("integration-test")
@Tag("application-layer")
@Transactional
@SpringBootTest
@ActiveProfiles("application-it")
@Import(JwtTestConfig.class)
// Disable security -> security should be tested in web/interfaces layer
@AutoConfigureMockMvc(addFilters = false)
public class BaseApplicationIT {}

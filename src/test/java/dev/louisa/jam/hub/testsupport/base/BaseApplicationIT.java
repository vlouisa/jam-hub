package dev.louisa.jam.hub.testsupport.base;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Tag("integration-test")
@Tag("application-layer")
@Transactional
@SpringBootTest
@ActiveProfiles("application-it")
// Disable security -> security should be tested in web/interfaces layer
@AutoConfigureMockMvc(addFilters = false)
public class BaseApplicationIT {}

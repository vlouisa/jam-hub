package dev.louisa.jam.hub.testsupport;

import dev.louisa.jam.hub.testsupport.config.InfrastructureLayerITConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Tag("integration-test")
@Tag("infrastructure-layer")
@SpringBootTest
@ActiveProfiles("infrastructure-it")
@Import(InfrastructureLayerITConfig.class)
@AutoConfigureMockMvc
public class BaseInfraStructureIT {}

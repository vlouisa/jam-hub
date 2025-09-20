package dev.louisa.jam.hub.infrastructure.security.jwt.provider;

import dev.louisa.jam.hub.application.user.port.outbound.JwtProvider;
import dev.louisa.jam.hub.infrastructure.Clock;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureIT;
import dev.louisa.jam.hub.testsupport.security.JwtClaimUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static dev.louisa.jam.hub.infrastructure.security.jwt.provider.JwtCustomClaimBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JwtProviderIT extends BaseInfraStructureIT {

    private static final Instant TODAY = Instant.parse("2025-09-01T12:36:48Z");
    @MockitoBean
    private Clock clock;
    
    private UUID userId;
    private List<UUID> bandIds;
    
    
    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        bandIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(clock.now()).thenReturn(TODAY);
    }
    
    @Test
    void shouldGenerateJwt() {
        final String jwt = jwtProvider.generate(
                userId,
                customClaims()
                        .singleValue("jam-hub:email", "herman.toothrot@dinky-island.mi2")
                        .multiValue("jam-hub:bands", bandIds)
        );

        assertThat(JwtClaimUtil.getClaims(jwt))
                .anyMatch(c -> c.startsWith("jti:"))
                .contains(
                        "sub:%s".formatted(userId),
                        "iss:urn:jam-hub:auth",
                        "aud:[jam-hub-service,jam-hub-gateway]",
                        "iat:%s".formatted(TODAY.getEpochSecond()),
                        "nbf:%s".formatted(TODAY.getEpochSecond()),
                        "exp:%s".formatted(TODAY.plusSeconds(600).getEpochSecond()),
                        "jam-hub:email:herman.toothrot@dinky-island.mi2",
                        "jam-hub:bands:[%s,%s]".formatted(bandIds.get(0), bandIds.get(1)));
    }
}
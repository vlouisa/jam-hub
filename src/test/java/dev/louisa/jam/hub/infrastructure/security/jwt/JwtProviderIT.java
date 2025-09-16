package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static dev.louisa.jam.hub.infrastructure.security.jwt.JwtCustomClaimBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderIT extends BaseInfraStructureIT {

    @Autowired
    private  JwtProvider jwtProvider;
    
    @Test
    void shouldGenerateJwt() {
        final String jwt = jwtProvider.generate(
                UUID.randomUUID(),
                customClaims()
                        .singleValue("jam-hub:email", "herman.toothrot@dinky-island.mi2")
                        .multiValue("jam-hub:bands", List.of(UUID.randomUUID(), UUID.randomUUID()))
                );
        assertThat(jwt).isNotNull();
    }

    private List<String> getJWTClaims(String rawJwt) {
        return JWT.decode(rawJwt).getClaims().entrySet().stream()
                .map(JwtProviderIT::getClaimValue)
                .toList();
    }

    private static String getClaimValue(Map.Entry<String, Claim> e) {
        final String claim = e.getKey() + ":" + e.getValue();
        return claim.replace("\"", "");
    }
}
package dev.louisa.jam.hub.infrastructure.security.jwt;

import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class JwtCustomClaimBuilderTest extends BaseInfraStructureTest {


    @Test
    void shouldBuildCustomClaims() {
        Map<String, Object> claims = JwtCustomClaimBuilder.customClaims()
                .singleValue("organization", "lucas-arts")
                .multiValue("characters", java.util.List.of("guybrush", "elaine"))
                .multiValue("drink", java.util.List.of("grogg"))
                .build();

        assertThat(claims).hasSize(3)
                .containsEntry("organization", "lucas-arts")
                .containsEntry("characters", List.of("guybrush", "elaine"))
                .containsEntry("drink", List.of("grogg"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "iss", "sub", "aud", "exp", "nbf", "iat", "jti",
            "ISS", "SUB", "AUD", "EXP", "NBF", "IAT", "JTI",
            "Iss", "Sub", "Aud", "Exp", "Nbf", "Iat", "Jti",
            "isS", "sUb", "AuD", "eXp", "nbF", "iaT", "jTi"

    })
    void shouldThrowWhenSingleValueClaimIsReservedClaim(String reservedClaim) {

        assertThatCode(() ->
                JwtCustomClaimBuilder.customClaims()
                        .singleValue(reservedClaim, "just-a-value")
                        .build())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_CLAIM_BUILDER_RESERVED_CLAIM.getMessage())
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("Claim name '%s' is reserved".formatted(reservedClaim));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenSingleValueClaimIsEmptyOrNull(String reservedClaim) {
        assertThatCode(() ->
                JwtCustomClaimBuilder.customClaims()
                        .singleValue(reservedClaim, "just-a-value")
                        .build())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_CLAIM_BUILDER_RESERVED_CLAIM.getMessage())
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("Claim name must not be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "iss", "sub", "aud", "exp", "nbf", "iat", "jti",
            "ISS", "SUB", "AUD", "EXP", "NBF", "IAT", "JTI",
            "Iss", "Sub", "Aud", "Exp", "Nbf", "Iat", "Jti",
            "isS", "sUb", "AuD", "eXp", "nbF", "iaT", "jTi"

    })
    void shouldThrowWhenMultiValueClaimIsReservedClaim(String reservedClaim) {

        assertThatCode(() ->
                JwtCustomClaimBuilder.customClaims()
                        .multiValue(reservedClaim, List.of("just-a-value"))
                        .build())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_CLAIM_BUILDER_RESERVED_CLAIM.getMessage())
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("Claim name '%s' is reserved".formatted(reservedClaim));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowWhenMultiValueClaimIsEmptyOrNull(String reservedClaim) {
        assertThatCode(() ->
                JwtCustomClaimBuilder.customClaims()
                        .multiValue(reservedClaim, List.of("just-a-value"))
                        .build())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(JWT_CLAIM_BUILDER_RESERVED_CLAIM.getMessage())
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("Claim name must not be null or blank");
    }

}

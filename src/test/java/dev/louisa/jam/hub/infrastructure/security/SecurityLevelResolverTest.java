package dev.louisa.jam.hub.infrastructure.security;

import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityLevelResolverTest extends BaseInfraStructureTest {
    private SecurityLevelResolver securityLevelResolver;
    
    @BeforeEach
    void setUp() {
        final PathMatcher pathMatcher = new AntPathMatcher();
        final UnsecuredEndpoints unsecuredEndpoints = new UnsecuredEndpoints(
                List.of(
                        Map.entry("/api/v1/unsecured-endpoint", "GET"))
        );
        securityLevelResolver = new SecurityLevelResolver(pathMatcher, unsecuredEndpoints);
    }

    @Test
    void shouldInformThatApiEndpointIsUnsecured() {
        assertThat(securityLevelResolver.isUnsecured("/api/v1/unsecured-endpoint", "GET"))
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource({
            "/api/v1/unsecured-endpoint, POST",
            "/api/v1/secured-endpoint, GET",
            "/api/v1/secured-endpoint, POST"
    })
    void shouldInformThatApiEndpointIsSecured(String requestUri, String method) {
        assertThat(securityLevelResolver.isUnsecured(requestUri, method))
                .isFalse();
    }

    @Test
    void shouldInformThatNonApiEndpointIsUnsecured() {
        assertThat(securityLevelResolver.isUnsecured("/public-endpoint", "GET"))
                .isTrue();
    }

}
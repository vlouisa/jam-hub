package dev.louisa.jam.hub.infrastructure.security.util;

import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import dev.louisa.jam.hub.testsupport.base.BaseInfraStructureTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.NO_BEARER_TOKEN;
import static dev.louisa.jam.hub.infrastructure.security.util.RequestTokenExtractor.bearerTokenFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class RequestTokenExtractorTest extends BaseInfraStructureTest {

    private MockHttpServletRequest request;
    
    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }
    
    @Test
    void shouldExtractBearerTokenFromRequest() {
        request.addHeader("Authorization", "Bearer NEdITbIbLgNSE9wjc9jVu6eQLwRqveDS/GLjtKV+Re8=");

        assertThat(bearerTokenFrom(request)).isEqualTo("NEdITbIbLgNSE9wjc9jVu6eQLwRqveDS/GLjtKV+Re8=");
    }

    @Test
    void shouldThrowWhenRequestHasNoAuthorizationHeader() {
        assertThatCode(() -> bearerTokenFrom(request))
        .isInstanceOf(SecurityException.class)
                .hasMessageContaining(NO_BEARER_TOKEN.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "Bearer",
            "Bearer ",
            "NEdITbIbLgNSE9wjc9jVu6eQLwRqveDS/GLjtKV+Re8="
    })
    void shouldThrowWhenHeaderIsMalformed(String headerValue) {
        request.addHeader("Authorization", headerValue);
        
        assertThatCode(() -> bearerTokenFrom(request))
        .isInstanceOf(SecurityException.class)
                .hasMessageContaining(NO_BEARER_TOKEN.getMessage());
    }

}

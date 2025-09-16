package dev.louisa.jam.hub.infrastructure.security.util;

import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import static dev.louisa.jam.hub.infrastructure.security.exception.SecurityError.NO_BEARER_TOKEN;


@Slf4j
public class RequestTokenExtractor {
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";

    public static String bearerTokenFrom(HttpServletRequest request) {
        var header = request.getHeader(AUTHORIZATION_HEADER_KEY);
        if (!isBearerToken(header)) {
            throw new SecurityException(NO_BEARER_TOKEN);
        }
        return header.substring(AUTHORIZATION_HEADER_VALUE_PREFIX.length());
    }

    private static boolean isBearerToken(String token) {
        return StringUtils.hasText(token) && token.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX);
    }
}

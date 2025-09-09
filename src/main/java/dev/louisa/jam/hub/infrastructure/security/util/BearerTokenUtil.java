package dev.louisa.jam.hub.infrastructure.security.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Optional;


@Slf4j
public class BearerTokenUtil {
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";

    public static Optional<String> extractTokenFrom(HttpServletRequest request) {
        return toBearerToken(request.getHeader(AUTHORIZATION_HEADER_KEY));
    }

    private static Optional<String> toBearerToken(String header) {
        return isBearerToken(header)
                ? Optional.of(header.substring(AUTHORIZATION_HEADER_VALUE_PREFIX.length()))
                : Optional.empty();
    }

    private static boolean isBearerToken(String token) {
        return StringUtils.hasText(token) && token.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX);
    }
}

package dev.louisa.jam.hub.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import java.util.Map;
import java.util.function.Predicate;

import static dev.louisa.jam.hub.infrastructure.security.SecurityConfig.UNSECURED_URI_LIST;

@Component
@Slf4j
public class SecurityLevelResolver {
    private final PathMatcher pathMatcher;

    public SecurityLevelResolver(@Lazy PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public boolean isUnsecured(String requestUri, String method) {
        if (!requestUri.startsWith("/api")) {
            return true;
        }

        return UNSECURED_URI_LIST.stream()
                .anyMatch(endpointInList(requestUri, method));
    }

    private Predicate<Map.Entry<String, String>> endpointInList(String requestUri, String method) {
        return entry -> entry.getValue().equalsIgnoreCase(method) &&
                pathMatcher.match(entry.getKey(), requestUri);
    }
}
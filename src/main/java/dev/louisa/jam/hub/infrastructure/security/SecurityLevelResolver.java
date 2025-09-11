package dev.louisa.jam.hub.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;

import java.util.Map;
import java.util.function.Predicate;


@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityLevelResolver {
    
    @Lazy
    private final PathMatcher pathMatcher;
    private final UnsecuredEndpoints unsecuredEndpoints;

    public boolean isUnsecured(String requestUri, String method) {
        if (!requestUri.startsWith("/api")) {
            return true;
        }

        return unsecuredEndpoints.list().stream()
                .anyMatch(endpointInList(requestUri, method));
    }

    private Predicate<Map.Entry<String, String>> endpointInList(String requestUri, String method) {
        return entry -> entry.getValue().equalsIgnoreCase(method) &&
                pathMatcher.match(entry.getKey(), requestUri);
    }
}
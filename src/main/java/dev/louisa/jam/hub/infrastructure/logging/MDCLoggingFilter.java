package dev.louisa.jam.hub.infrastructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class MDCLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", "N/A");
            
            log.info("Inbound call - request: [uri={}, method={}]", request.getRequestURI(), request.getMethod());
            filterChain.doFilter(request, response);
            log.info("Inbound call - response: [uri={}, method={}, status={}]", request.getRequestURI(), request.getMethod(), response.getStatus());
        } finally {
            MDC.clear();
        }
    }
}
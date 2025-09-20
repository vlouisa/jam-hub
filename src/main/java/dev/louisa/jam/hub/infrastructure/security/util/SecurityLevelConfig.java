package dev.louisa.jam.hub.infrastructure.security.util;

import dev.louisa.jam.hub.infrastructure.security.UnsecuredEndpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class SecurityLevelConfig {

    @Bean
    public UnsecuredEndpoints unsecuredEndpoints() {
        return new UnsecuredEndpoints(List.of(
                Map.entry("/api/v1/registrations", "POST"),
                Map.entry("/api/v1/registrations/*/verify", "POST"),
                Map.entry("/api/v1/auth/login", "POST")
        ));
    }
}

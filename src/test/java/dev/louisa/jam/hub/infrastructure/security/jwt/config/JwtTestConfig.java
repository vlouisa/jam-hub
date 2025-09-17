package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyCreator;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JwtTestConfig {

    @Bean
    public JwtKeys jwtKeys(JwtProperties properties) {
        return new JwtKeys(
                properties,
                JwtKeyCreator.fromBundle("2025.09.17.170752"),
                JwtKeyCreator.fromBundle("2025.09.17.170803")
        );
    }
}

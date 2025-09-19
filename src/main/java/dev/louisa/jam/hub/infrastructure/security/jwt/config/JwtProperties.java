package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt-key-resolver")
public class JwtProperties {
    private String activeBundle;
    private String masterKey;
}
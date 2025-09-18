package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

    @Bean
    @Profile("!interface-it & !infrastructure-it & !application-it")
    public JwtKeys jwtKeys(JwtProperties properties) {
        return new JwtKeys(
                properties,
                JwtKeyCreator.fromBundle("2024.11.01.171244"),
                JwtKeyCreator.fromBundle("2025.09.16.081255"),
                JwtKeyCreator.fromBundle("2025.09.16.094712")
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtKeys jwtKeys) {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(jwtKeys.toJWKSet()));
    }
}

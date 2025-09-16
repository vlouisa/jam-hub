package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import dev.louisa.jam.hub.infrastructure.security.util.JwtKeyCreate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {

    @Bean
    public JwtKeys jwtKeys(JwtProperties properties) {
        return new JwtKeys(
                properties,
                JwtKeyCreate.fromBundle("2024.11.01.171244"),
                JwtKeyCreate.fromBundle("2025.09.16.081255"),
                JwtKeyCreate.fromBundle("2025.09.16.094712")
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtKeys jwtKeys) {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(jwtKeys.toJWKSet()));
    }
}

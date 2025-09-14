package dev.louisa.jam.hub.infrastructure.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.louisa.jam.hub.infrastructure.security.util.RSAKeyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.util.List;

@Configuration
public class JwtProviderConfig {
    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JWKSet jwkSet(RSAKeyBuilder rsaKeyBuilder) {
        return new JWKSet(
                List.of(
                        rsaKeyBuilder.createRSAKeyFromBundle("2024.11.01.170938"),
                        rsaKeyBuilder.createRSAKeyFromBundle("2024.11.01.171244")
                ));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}

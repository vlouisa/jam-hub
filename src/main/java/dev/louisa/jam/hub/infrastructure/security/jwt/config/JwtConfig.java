package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.util.List;

@Configuration
public class JwtConfig {
    
    @Profile("!interface-it & !infrastructure-it & !application-it")
    @Bean
    public JwtKeys jwtKeys(JwtKeyRepository repository, JwtKeyService jwtKeyService) throws Exception {
        List<JwtKey> allKeys = repository.findAll().stream()
                .map(jwtKeyService::toJwtKey)
                .toList();
        return new JwtKeys(allKeys);
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtKeys jwtKeys) {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(jwtKeys.toJWKSet()));
    }
}

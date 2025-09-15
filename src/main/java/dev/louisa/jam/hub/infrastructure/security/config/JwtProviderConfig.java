package dev.louisa.jam.hub.infrastructure.security.config;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import dev.louisa.jam.hub.infrastructure.security.jwt.JHubJwtValidator;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtKeySource;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtKeys;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtValidator;
import dev.louisa.jam.hub.infrastructure.security.util.RSAKeyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtProviderConfig {

    @Bean
    public JwtKeys jwtKeys(RSAKeyBuilder rsaKeyBuilder) {
        return new JwtKeys(
                rsaKeyBuilder.createRSAKeyFromBundle("2024.11.01.170938"),
                rsaKeyBuilder.createRSAKeyFromBundle("2024.11.01.171244")
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtKeySource jwtKeySource) {
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(jwtKeySource.toJWKSet()));
    }
    
    @Bean
    public JwtValidator jwtValidator(JwtKeySource JwtKeySource) {
        return new JHubJwtValidator(JwtKeySource.getFirstPublicKey());
    }
}

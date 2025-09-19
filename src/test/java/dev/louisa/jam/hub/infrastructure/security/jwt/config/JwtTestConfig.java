package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyEntity;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyRepository;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeys;
import dev.louisa.jam.hub.testsupport.jwt.JwtKeyGenerator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@TestConfiguration
public class JwtTestConfig {
    private static final String BUNDLE_ID = "2025.09.17.170752";

    @Bean
    public JwtKeys jwtKeys(JwtProperties properties, JwtKeyRepository repository, JwtKeyService service) throws Exception {
        repository.deleteAll();

        JwtKeyGenerator keyGenerator = new JwtKeyGenerator(repository);
        keyGenerator.generate(properties.getMasterKey());

        List<JwtKeyEntity> all = repository.findAll();
        List<JwtKey> allKeys = all.stream()
                .map(service::toJwtKey)
                .toList();
        return new JwtKeys(allKeys);
    }
}

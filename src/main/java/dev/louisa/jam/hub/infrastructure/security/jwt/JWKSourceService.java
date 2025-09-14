package dev.louisa.jam.hub.infrastructure.security.jwt;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JWKSourceService {
    private static final JWKSelector JWK_MATCHER =
            new JWKSelector(
                    new JWKMatcher.Builder()
                            .keyUse(KeyUse.SIGNATURE)
                            .keyType(KeyType.RSA)
                            .build()
            );

    private final JWKSource<SecurityContext> jwkSource;

    public String getKidFromFirstSigningKey() {
        try {
            return jwkSource
                    .get(JWK_MATCHER, null)
                    .getFirst()
                    .getKeyID();
        } catch (KeySourceException e) {
            throw new RuntimeException(e);
        }
    }
}
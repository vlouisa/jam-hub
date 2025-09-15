package dev.louisa.jam.hub.infrastructure.security.util;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReaderParameters.*;

@Component
@Slf4j
public class RSAKeyBuilder {

    public JwtKey createRSAKeyFromBundle(String bundleName) {
        final String kid = keyIdFileLocation(bundleName);
        return JwtKey.builder()
                .kid(kid)
                .rsaKey(createRSAKey(kid, privateKeyFileLocation(bundleName), publicKeyFileLocation(bundleName)))
                .build();
    }

    private RSAKey createRSAKey(String kid, String privateKeyFileName, String publicKeyFileName) {
        log.info("KID : {}", kid);

        final KeyPair keyPair = new KeyPair(
                RSAKeyReader.readPublicKeyFromFile(publicKeyFileName),
                RSAKeyReader.readPrivateKeyFromFile(privateKeyFileName)
        );

        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(kid)
                .keyUse(KeyUse.SIGNATURE)
                .build();
    }
}
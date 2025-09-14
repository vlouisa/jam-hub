package dev.louisa.jam.hub.infrastructure.security.util;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReaderParameters.*;

@Component
@Slf4j
public class RSAKeyBuilder {

    public RSAKey createRSAKeyFromBundle(String bundleName) {
        return createRSAKey(
                keyIdFileLocation(bundleName),
                privateKeyFileLocation(bundleName),
                publicKeyFileLocation(bundleName));
    }

    private RSAKey createRSAKey(String keyIdFileName, String privateKeyFileName, String publicKeyFileName) {
        final String kid = RSAKeyReader.readKeyIdFromFile(keyIdFileName);
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
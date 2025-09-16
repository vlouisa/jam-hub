package dev.louisa.jam.hub.infrastructure.security.util;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.JwtKey;
import lombok.extern.slf4j.Slf4j;


import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader.readKeyIdFromFile;
import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReaderParameters.*;

@Slf4j
public class JwtKeyCreate {
    public static JwtKey fromBundle(String bundleName) {
        final String kid = readKeyIdFromFile(keyIdFileLocation(bundleName));
        return JwtKey.builder()
                .kid(kid)
                .bundleName(bundleName)
                .rsaKey(createRSAKey(kid, privateKeyFileLocation(bundleName), publicKeyFileLocation(bundleName)))
                .build();
    }

    private static RSAKey createRSAKey(String kid, String privateKeyFileName, String publicKeyFileName) {
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
package dev.louisa.jam.hub.testsupport.jwt;

import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader;
import lombok.extern.slf4j.Slf4j;


import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.regex.Pattern;

import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReader.readKeyIdFromFile;
import static dev.louisa.jam.hub.infrastructure.security.util.RSAKeyReaderParameters.*;

@Slf4j
public class JwtKeyCreator {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    public static JwtKey fromBundle(String bundleName) {
        final String kid = readKeyIdFromFile(keyIdFileLocation(bundleName));
        validate(kid, bundleName);
        log.info("Creating JwtKey with kid '{}' from bundle '{}'", kid, bundleName);
        
        return JwtKey.builder()
                .kid(UUID.fromString(kid))
                .status("ACTIVE")
                .rsaKey(createRSAKey(kid, privateKeyFileLocation(bundleName), publicKeyFileLocation(bundleName)))
                .build();
    }

    private static void validate(String kid, String bundleName) {
        if (!UUID_REGEX.matcher(kid).matches()) {
            throw new IllegalStateException(".kid file '%s.kid' must contain a valid UUID v4".formatted(bundleName));
        }    }

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
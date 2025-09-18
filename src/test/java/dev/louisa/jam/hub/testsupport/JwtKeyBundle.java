package dev.louisa.jam.hub.testsupport;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Slf4j
public class JwtKeyBundle {
    /* IMPORTANT NOTE:
     * This class has been located in the test java packages because we don't want to deploy an ordinary class with a
     * 'main' method to production. This class should also not be used as part of your production code, because from
     * risk perspective we should not be able to generate key bundles on runtime!!!
     */


    private static final String KEY_BUNDLE_FILE_LOCATION = "src/main/resources/keys/";

    /**
     * This utility method generates a new pair of keys, private key (PKCS#8) / public key(X.509), and creates
     * the key pair bundle files in the folder designated in KEY_BUNDLE_FILE_LOCATION.
     *
     **/
    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        final KeyPair keyPair = generateKeyPair();
        final String bundleId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HHmmss"));

        log.info("BundleID={}", bundleId);
        log.info("Private key format={}", keyPair.getPrivate().getFormat());
        log.info("Private key value={}", toBase64(keyPair.getPrivate()));
        log.info("Public key format={}", keyPair.getPublic().getFormat());
        log.info("Public key value={}", toBase64(keyPair.getPublic()));

        write(bundleId, UUID.randomUUID().toString(), "kid");
        write(bundleId, toBase64(keyPair.getPrivate()), "key");
        write(bundleId, toBase64(keyPair.getPublic()), "key.pub");
    }

    private static void write(String bundleID, String data, String fileSuffix) {
        try {
            Files.write(Paths.get(KEY_BUNDLE_FILE_LOCATION + bundleID + "." + fileSuffix), data.getBytes());
        } catch (IOException e) {
            log.error("Error while writing '{}' file for bundle {}:", fileSuffix, bundleID);
        }
    }

    private static String toBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    private static String toBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}

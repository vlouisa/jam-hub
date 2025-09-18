package dev.louisa.jam.hub.infrastructure.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyReader {

    public static String readKeyIdFromFile(String kidFileName) {
        try {
            return readResource(kidFileName);
        } catch (Exception e) {
            final String message = String.format("Error while reading private key file [key id file=%s]",kidFileName);
            throw new RuntimeException(message, e);
        }
    }

    public static RSAPublicKey readPublicKeyFromFile(String publicKeyFileName) {
        try {
            final String publicKeyContent = readResource(publicKeyFileName);
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(keySpecX509);
        } catch (Exception e) {
            final String message = String.format("Error while reading private key file [public key file=%s]", publicKeyFileName);
            throw new RuntimeException(message, e);
        }
    }

    public static RSAPrivateKey readPrivateKeyFromFile(String privateKeyFileName) {
        try {
            String privateKeyContent = readResource(privateKeyFileName);
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);
        } catch (Exception e) {
            final String message = String.format("Error while reading private key file [private key file=%s]", privateKeyFileName);
            throw new RuntimeException(message, e);
        }
    }

    private static String readResource(String relativePath) throws IOException {
        try (InputStream inputStream = RSAKeyReader.class.getClassLoader().getResourceAsStream(relativePath)) {
            if (inputStream == null) {
                final String message = String.format("Error while reading resource [relative path=%s]", relativePath);
                throw new RuntimeException(message);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}

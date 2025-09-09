package dev.louisa.jam.hub.infrastructure.security.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyReader {

    public static RSAPublicKey readPublicKeyFromFile(String publicKeyFileName) {
        try {
            String publicKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(publicKeyFileName).toURI())));
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(keySpecX509);
        } catch (Exception e) {
            throw new RuntimeException("Error while reading public key file", e);
        }
    }
    public static RSAPrivateKey readPrivateKeyFromFile(String privateKeyFileName) {
        try {
            String privateKeyContent = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(privateKeyFileName).toURI())));
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(keySpecPKCS8);
        } catch (Exception e) {
            throw new RuntimeException("Error while reading private key file", e);
        }
    }
}

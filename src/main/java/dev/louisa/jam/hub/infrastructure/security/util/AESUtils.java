package dev.louisa.jam.hub.infrastructure.security.util;

import dev.louisa.jam.hub.infrastructure.security.jwt.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtils {

    private final SecretKeySpec secretKey;


    @Autowired
    public AESUtils(JwtProperties jwtProperties) {
        this(jwtProperties.getMasterKey());
    }

    public AESUtils(String masterKey) {
        byte[] keyBytes = Base64.getDecoder().decode(masterKey);
        if (keyBytes.length != 32) { // AES-256 requires 32 bytes
            throw new IllegalArgumentException("Master key must be 32 bytes for AES-256");
        }
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
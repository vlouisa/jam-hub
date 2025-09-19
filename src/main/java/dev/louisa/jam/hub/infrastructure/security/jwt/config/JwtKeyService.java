package dev.louisa.jam.hub.infrastructure.security.jwt.config;

import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyEntity;
import dev.louisa.jam.hub.infrastructure.security.util.AESUtils;
import dev.louisa.jam.hub.infrastructure.security.util.PemUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
@RequiredArgsConstructor
public class JwtKeyService {
    private final AESUtils aes;
    
    public JwtKey toJwtKey(JwtKeyEntity entity) {
        try {
            // 1. Decrypt AES -> gives back Base64 string
            String decryptedPrivate = aes.decrypt(entity.getPrivateKey());
            String decryptedPublic = aes.decrypt(entity.getPublicKey());

            // 2. Decode Base64 into raw key bytes and rebuild Java keys
            var privateKey = PemUtils.parsePrivateKey(decryptedPrivate);
            var publicKey = PemUtils.parsePublicKey(decryptedPublic);

            // 3. Wrap into Nimbus RSAKey for JWT handling
            RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) publicKey)
                    .privateKey((RSAPrivateKey) privateKey)
                    .keyID(entity.getKid().toString())
                    .build();

            return new JwtKey(entity.getKid(), entity.getStatus(), rsaKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse key: " + entity.getKid(), e);
        }
    }
}

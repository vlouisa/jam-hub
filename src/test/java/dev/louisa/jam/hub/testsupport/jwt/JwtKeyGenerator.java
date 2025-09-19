package dev.louisa.jam.hub.testsupport.jwt;

import com.nimbusds.jose.jwk.RSAKey;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyEntity;
import dev.louisa.jam.hub.infrastructure.security.jwt.common.JwtKeyRepository;
import dev.louisa.jam.hub.infrastructure.security.util.AESUtils;
import lombok.RequiredArgsConstructor;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
public class JwtKeyGenerator {
    private final JwtKeyRepository repository;

    public static void main(String[] args) throws Exception {
        JwtKeyGenerator generator = new JwtKeyGenerator(null);

        String masterKey = "RvLLoz/Zh/t2ba8FQY+SNfrnLU2kg5M9pnD2G9RqS1E=";
        generator.generate(masterKey);
    }


    public void generate(String masterKey) throws Exception {

        // Generate RSA 2048-bit key pair
        final KeyPair kp = generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

        // Build RSAKey from Nimbus JOSE
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        // Encode keys in Base64
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        // Encrypt keys using AES
        final AESUtils aes = new AESUtils(masterKey);
        String encryptedPublicKey = aes.encrypt(publicKeyStr);
        String encryptedPrivateKey = aes.encrypt(privateKeyStr);

        printKeyInfo(masterKey, rsaKey, encryptedPublicKey, encryptedPrivateKey);
        saveToDb(rsaKey, encryptedPublicKey, encryptedPrivateKey);

    }

    private void saveToDb(RSAKey rsaKey, String encryptedPublicKey, String encryptedPrivateKey) {
        if (repository == null) {
            System.out.println("No repository available, skipping persistence of generated keys");
            System.out.println("------------------------------------------- END OF KEY GENERATION --------------------------------------------");
        }
        System.out.println("Persisting the generated keys to the DB");
        JwtKeyEntity entity = new JwtKeyEntity();
        entity.setKid(UUID.fromString(rsaKey.getKeyID()));
        entity.setPublicKey(encryptedPublicKey);
        entity.setPrivateKey(encryptedPrivateKey);
        entity.setStatus("ACTIVE"); // Only one active key at a time
        System.out.println("------------------------------------------- END OF KEY GENERATION --------------------------------------------");

        repository.save(entity);
    }

    private static void printKeyInfo(String masterKey, RSAKey rsaKey, String encryptedPublicKey, String encryptedPrivateKey) {
        System.out.println("----------------------------------------------- KEY GENERATION -----------------------------------------------");
        System.out.println("Generating new Keys for JWT (encrypted with AES). ");
        System.out.printf("Session id: '%s'%n", UUID.randomUUID());
        System.out.printf("Master Key: '%s'%n", masterKey);
        System.out.println("-------------------------------------------------- JWT kid --------------------------------------------------");
        System.out.println(rsaKey.getKeyID());
        System.out.println("------------------------------------------ Public Key (encrypted) -------------------------------------------");
        System.out.println(encryptedPublicKey);
        System.out.println("------------------------------------------ Private Key (encrypted) -------------------------------------------");
        System.out.println(encryptedPrivateKey);
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        return kp;
    }
}

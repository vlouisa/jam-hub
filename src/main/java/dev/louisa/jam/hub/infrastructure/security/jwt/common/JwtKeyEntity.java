package dev.louisa.jam.hub.infrastructure.security.jwt.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "jwt_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtKeyEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID kid;

    @Column(nullable = false, length = 4096)
    private String privateKey; // AES-encrypted

    @Column(nullable = false, length = 4096)
    private String publicKey;  // AES-encrypted

    @Column(nullable = false)
    private String status; // "ACTIVE" or "INACTIVE"
}

package dev.louisa.jam.hub.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

public record Password(String hash) {

    public Password {
        Objects.requireNonNull(hash, "Hash cannot be null");
    }

    /** Check if a candidate plain text matches this password. */
    public boolean matches(String candidate, PasswordEncoder encoder) {
        return encoder.matches(candidate, this.hash);
    }
}
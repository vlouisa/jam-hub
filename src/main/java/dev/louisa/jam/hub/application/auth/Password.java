package dev.louisa.jam.hub.application.auth;

import dev.louisa.jam.hub.application.auth.port.outbound.PasswordHasher;
import dev.louisa.jam.hub.domain.user.HashedPassword;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityError;
import dev.louisa.jam.hub.infrastructure.security.exception.SecurityException;

import java.util.regex.Pattern;

public record Password(String value) {

    public static Password fromString(String rawPassword) {
        return new Password(rawPassword);
    }
    
    // At least one uppercase, one lowercase, one digit, one special char, min 8 chars
    private static final Pattern COMPLEXITY_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");

    public Password {
        if (value == null || value.isBlank()) {
            throw new SecurityException(SecurityError.INVALID_CREDENTIALS,
                new IllegalArgumentException("Password cannot be empty"));
        }
        if (!COMPLEXITY_PATTERN.matcher(value).matches()) {
            throw new SecurityException(SecurityError.INVALID_CREDENTIALS, 
                new IllegalArgumentException("Password does not meet complexity requirements"));
        }
    }

    public boolean matches(HashedPassword hashedPassword, PasswordHasher passwordHasher) {
        return passwordHasher.matches(value, hashedPassword.value());
    }

    public HashedPassword hash(PasswordHasher hasher) {
        return new HashedPassword(hasher.hash(value));
    }

    @Override
    public String toString() {
        return "********"; // don’t leak raw password
    }
}

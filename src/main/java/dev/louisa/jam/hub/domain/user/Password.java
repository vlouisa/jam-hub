package dev.louisa.jam.hub.domain.user;

import dev.louisa.jam.hub.application.user.port.outbound.PasswordHasher;

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
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!COMPLEXITY_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Password does not meet complexity requirements");
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

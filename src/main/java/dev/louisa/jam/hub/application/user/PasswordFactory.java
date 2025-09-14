package dev.louisa.jam.hub.application.user;

import dev.louisa.jam.hub.domain.user.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordFactory {

    private final PasswordEncoder passwordEncoder;

    public Password from(String plainText) {
        applyPolicy(plainText);
        return new Password(passwordEncoder.encode(plainText));
    }

    /** Validate password according to business rules */
    private void applyPolicy(String plainText) {
        if (plainText == null || plainText.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }
}
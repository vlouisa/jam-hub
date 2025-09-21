package dev.louisa.jam.hub.application.auth.port.outbound;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}

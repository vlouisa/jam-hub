package dev.louisa.jam.hub.api.auth;

import dev.louisa.jam.hub.domain.common.EmailAddress;
import dev.louisa.jam.hub.application.auth.Credentials;
import dev.louisa.jam.hub.application.auth.Password;
import lombok.Builder;

@Builder
public record LoginRequest(String username, String password) {
 
    Credentials toCredentials() {
        return Credentials.builder()
                .emailAddress(EmailAddress.from(username))
                .password(Password.fromString(password))
                .build();
    }   
}

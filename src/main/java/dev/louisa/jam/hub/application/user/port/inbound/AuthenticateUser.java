package dev.louisa.jam.hub.application.user.port.inbound;

import dev.louisa.jam.hub.domain.user.Authentication;
import dev.louisa.jam.hub.domain.user.Credentials;

public interface AuthenticateUser {

    Authentication authenticate(Credentials credentials);
}

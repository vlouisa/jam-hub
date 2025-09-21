package dev.louisa.jam.hub.application.auth.port.inbound;

import dev.louisa.jam.hub.application.auth.Authentication;
import dev.louisa.jam.hub.application.auth.Credentials;

public interface AuthenticateUser {

    Authentication authenticate(Credentials credentials);
}

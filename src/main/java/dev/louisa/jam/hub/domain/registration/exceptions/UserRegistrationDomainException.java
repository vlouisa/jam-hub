package dev.louisa.jam.hub.domain.registration.exceptions;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class UserRegistrationDomainException extends JamHubException {

    public UserRegistrationDomainException(JamHubError error) {
        this(error, List.of());
    }

    public UserRegistrationDomainException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public UserRegistrationDomainException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public UserRegistrationDomainException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

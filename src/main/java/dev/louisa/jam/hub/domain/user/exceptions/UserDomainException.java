package dev.louisa.jam.hub.domain.user.exceptions;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class UserDomainException extends JamHubException {

    public UserDomainException(JamHubError error) {
        this(error, List.of());
    }

    public UserDomainException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public UserDomainException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public UserDomainException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

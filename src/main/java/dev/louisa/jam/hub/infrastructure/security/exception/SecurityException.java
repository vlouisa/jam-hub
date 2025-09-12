package dev.louisa.jam.hub.infrastructure.security.exception;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class SecurityException extends JamHubException {

    public SecurityException(JamHubError error) {
        this(error, List.of());
    }

    public SecurityException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public SecurityException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public SecurityException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

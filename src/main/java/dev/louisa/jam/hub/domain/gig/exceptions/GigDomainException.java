package dev.louisa.jam.hub.domain.gig.exceptions;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class GigDomainException extends JamHubException {

    public GigDomainException(JamHubError error) {
        this(error, List.of());
    }

    public GigDomainException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public GigDomainException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public GigDomainException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

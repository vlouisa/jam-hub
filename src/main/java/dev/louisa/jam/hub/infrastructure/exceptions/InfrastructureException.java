package dev.louisa.jam.hub.infrastructure.exceptions;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class InfrastructureException extends JamHubException {

    public InfrastructureException(JamHubError error) {
        this(error, List.of());
    }

    public InfrastructureException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public InfrastructureException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public InfrastructureException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

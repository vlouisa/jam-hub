package dev.louisa.jam.hub.application.exceptions;

import dev.louisa.jam.hub.domain.shared.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class ApplicationException extends JamHubException {

    public ApplicationException(JamHubError error) {
        this(error, List.of());
    }

    public ApplicationException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public ApplicationException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public ApplicationException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

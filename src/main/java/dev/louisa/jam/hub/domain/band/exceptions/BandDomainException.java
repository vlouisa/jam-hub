package dev.louisa.jam.hub.domain.band.exceptions;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class BandDomainException extends JamHubException {

    public BandDomainException(JamHubError error) {
        this(error, List.of());
    }

    public BandDomainException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public BandDomainException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public BandDomainException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

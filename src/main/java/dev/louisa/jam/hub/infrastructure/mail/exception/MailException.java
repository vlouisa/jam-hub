package dev.louisa.jam.hub.infrastructure.mail.exception;

import dev.louisa.jam.hub.domain.common.Id;
import dev.louisa.jam.hub.exceptions.JamHubError;
import dev.louisa.jam.hub.exceptions.JamHubException;

import java.util.List;

public class MailException extends JamHubException {

    public MailException(JamHubError error) {
        this(error, List.of());
    }

    public MailException(JamHubError error, Throwable cause) {
        this(error, List.of(), cause);
    }
    public MailException(JamHubError error, List<Id> contexts) {
        this(error, contexts, null);
    }
    public MailException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(error, contexts, cause);
    }
}

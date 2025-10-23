package dev.louisa.jam.hub.exceptions;

import java.util.List;

public class GenericException extends JamHubException {

    public GenericException(JamHubError error, Throwable cause) {
        super(error, List.of(), cause);
    }
}

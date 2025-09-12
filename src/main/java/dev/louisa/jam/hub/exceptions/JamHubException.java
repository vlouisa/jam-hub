package dev.louisa.jam.hub.exceptions;

import dev.louisa.jam.hub.domain.common.Id;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;


@Getter
public abstract class JamHubException extends RuntimeException {
    private final JamHubError error;
    private final String shortMessage;
    private final HttpStatus httpStatus;
    private final List<Id> contexts;

    public JamHubException(JamHubError error, List<Id> contexts, Throwable cause) {
        super(formatFullMessage(error, contexts), cause);
        this.error = error;
        this.shortMessage = error.getMessage();
        this.contexts = contexts;
        this.httpStatus = error.getHttpStatus();
    }

    private static String formatFullMessage(JamHubError error, List<Id> contexts) {
        StringBuilder sb = new StringBuilder();
        sb.append(error.getDomainCode()).append("-").append(error.getErrorCode()).append(" | ");
        sb.append(error.getHttpStatus()).append(" | ");
        sb.append(error.getMessage());
        possiblyAddContext(contexts, sb);
        return sb.toString();
    }

    private static void possiblyAddContext(List<Id> contexts, StringBuilder sb) {
        if (contexts != null && !contexts.isEmpty()) {
            sb.append(" | Context: ");
            String contextStr = contexts.stream()
                    .map(Id::toString)
                    .collect(java.util.stream.Collectors.joining(", "));
            sb.append(contextStr);
        }
    }
}

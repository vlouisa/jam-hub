package dev.louisa.jam.hub.exceptions;

import dev.louisa.jam.hub.domain.shared.Id;
import lombok.Getter;

import java.util.List;


@Getter
public abstract class JamHubException extends RuntimeException {
	private final JamHubError error;
	private final String message;
	private final List<Id> contexts;
	
	public JamHubException(JamHubError error, List<Id> contexts, Throwable cause) {
		super(formatFullMessage(error, contexts), cause);
		this.error = error;
		this.contexts = contexts;
		this.message = error.getMessage();
	}

	private static String formatFullMessage(JamHubError error, List<Id> contexts) {
		StringBuilder sb = new StringBuilder();
		sb.append(error.getDomainCode()).append("-").append(error.getErrorCode()).append(" | ");
		sb.append(error.getMessage());
		if (contexts != null && !contexts.isEmpty()) {
			sb.append(" | Context: ");
			for (Id context : contexts) {
				sb.append(context.toString()).append(", ");
			}
			// Remove last comma and space
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}}

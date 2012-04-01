package org.kalibro;

public class KalibroError extends KalibroException {

	public static final String MESSAGE_PREFIX = "Please report this bug: ";

	public KalibroError(String message) {
		super(MESSAGE_PREFIX + message);
	}

	public KalibroError(String message, Throwable cause) {
		super(MESSAGE_PREFIX + message, cause);
	}
}
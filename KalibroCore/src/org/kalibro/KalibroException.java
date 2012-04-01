package org.kalibro;

import org.kalibro.core.util.StackTracePrinter;

// TODO Create KalibroError for exceptions that should never happen, indicating bugs 
public class KalibroException extends RuntimeException {

	public KalibroException(String message) {
		super(message);
	}

	public KalibroException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getPrintedStackTrace() {
		return new StackTracePrinter().printStackTrace(this);
	}
}
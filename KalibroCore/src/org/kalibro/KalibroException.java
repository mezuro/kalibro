package org.kalibro;

/**
 * Exception thrown by Kalibro. Allows to wrap other exceptions, imitating them.
 * 
 * @author Carlos Morais
 */
public class KalibroException extends RuntimeException {

	private String targetString;

	public KalibroException(String message) {
		super(message);
	}

	public KalibroException(String message, Throwable cause) {
		super(message, cause);
	}

	public KalibroException(Throwable target) {
		super(target.getMessage(), target.getCause());
		setStackTrace(target.getStackTrace());
		targetString = target.toString();
	}

	@Override
	public String toString() {
		if (targetString == null)
			return super.toString();
		return targetString;
	}
}
package org.kalibro;

/**
 * Exception thrown by Kalibro.
 * 
 * @author Carlos Morais
 */
public class KalibroException extends RuntimeException {

	public KalibroException(String message) {
		super(message);
	}

	public KalibroException(String message, Throwable cause) {
		super(message, cause);
	}
}
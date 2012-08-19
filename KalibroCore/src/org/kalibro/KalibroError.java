package org.kalibro;

/**
 * An exception that indicates Kalibro has an error (bug).
 * 
 * @author Carlos Morais
 */
public class KalibroError extends Error {

	public KalibroError(String message) {
		super(message);
	}

	public KalibroError(String message, Throwable cause) {
		super(message, cause);
	}
}
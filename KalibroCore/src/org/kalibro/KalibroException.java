package org.kalibro;

/**
 * Designed to be the only exception thrown by Kalibro, wrapping checked exceptions (KalibroException is unchecked).
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
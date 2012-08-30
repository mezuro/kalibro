package org.kalibro;

/**
 * Indicates Kalibro has a bug to be fixed. Every instructions that throws it should be dead code.
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
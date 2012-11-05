package org.kalibro.dto;

import org.kalibro.KalibroException;

/**
 * Data transfer object for {@link Throwable}.
 * 
 * @author Carlos Morais
 */
public abstract class ThrowableDto extends DataTransferObject<Throwable> {

	@Override
	public Throwable convert() {
		KalibroException wrapper = new KalibroException(message(), cause());
		wrapper.setStackTrace(stackTrace());
		set(wrapper, "targetString", targetString());
		return wrapper;
	}

	public abstract String targetString();

	public abstract String message();

	public abstract Throwable cause();

	public abstract StackTraceElement[] stackTrace();
}
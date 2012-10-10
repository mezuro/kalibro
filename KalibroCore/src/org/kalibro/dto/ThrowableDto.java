package org.kalibro.dto;

import java.lang.reflect.Constructor;

import org.kalibro.KalibroError;

/**
 * Data transfer object for {@link Throwable}.
 * 
 * @author Carlos Morais
 */
public abstract class ThrowableDto extends DataTransferObject<Throwable> {

	@Override
	public Throwable convert() {
		try {
			return doConvert();
		} catch (Exception exception) {
			throw new KalibroError("Could not convert Throwable.", exception);
		}
	}

	private Throwable doConvert() throws Exception {
		Constructor<?> constructor = Class.forName(throwableClass()).getConstructor(String.class);
		Throwable throwable = (Throwable) constructor.newInstance(detailMessage());
		throwable.setStackTrace(stackTrace());
		convertCause(throwable);
		return throwable;
	}

	private void convertCause(Throwable throwable) {
		Throwable cause = cause();
		if (cause != null)
			throwable.initCause(cause);
	}

	public abstract String throwableClass();

	public abstract String detailMessage();

	public abstract Throwable cause();

	public abstract StackTraceElement[] stackTrace();
}
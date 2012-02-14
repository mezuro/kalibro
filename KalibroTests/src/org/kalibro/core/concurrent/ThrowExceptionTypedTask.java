package org.kalibro.core.concurrent;

public class ThrowExceptionTypedTask<T> extends TypedTask<T> {

	@Override
	public T generateResult() throws Exception {
		throw new Exception();
	}
}
package org.kalibro.core.concurrent;

class ThrowExceptionTypedTask<T> extends TypedTask<T> {

	@Override
	public T performAndGetResult() throws Exception {
		throw new Exception();
	}
}
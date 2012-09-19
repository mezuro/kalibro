package org.kalibro.core.concurrent;

class ThrowExceptionTypedTask<T> extends TypedTask<T> {

	@Override
	public T compute() throws Exception {
		throw new Exception();
	}
}
package org.kalibro.core.concurrent;

final class RetrieveResultTask<T> extends Task<T> {

	private T result;

	RetrieveResultTask(T result) {
		this.result = result;
	}

	@Override
	protected T compute() {
		return result;
	}
}
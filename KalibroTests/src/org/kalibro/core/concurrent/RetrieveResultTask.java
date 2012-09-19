package org.kalibro.core.concurrent;

final class RetrieveResultTask<T> extends Task<T> {

	private T resultToBeRetrieved;

	RetrieveResultTask(T result) {
		resultToBeRetrieved = result;
	}

	@Override
	public T compute() {
		return resultToBeRetrieved;
	}
}
package org.kalibro.core.concurrent;

class RetrieveResultTask<T> extends Task<T> {

	private T resultToBeRetrieved;

	protected RetrieveResultTask(T result) {
		resultToBeRetrieved = result;
	}

	@Override
	public T compute() {
		return resultToBeRetrieved;
	}
}
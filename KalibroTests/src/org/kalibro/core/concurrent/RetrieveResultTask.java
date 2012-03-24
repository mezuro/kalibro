package org.kalibro.core.concurrent;

class RetrieveResultTask<T> extends TypedTask<T> {

	private T resultToBeRetrieved;

	protected RetrieveResultTask(T result) {
		resultToBeRetrieved = result;
	}

	@Override
	public T performAndGetResult() {
		return resultToBeRetrieved;
	}
}